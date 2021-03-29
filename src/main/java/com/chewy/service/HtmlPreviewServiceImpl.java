package com.chewy.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.chewy.Channel;
import com.chewy.Context;
import com.chewy.HtmlPreviewThreadLocal;
import com.chewy.dao.MessageTemplateDAO;
import com.chewy.domain.email.EmailMessageData;
import com.chewy.domain.events.NotificationEvent;
import com.chewy.domain.notfctr.MessageTemplate;
import com.chewy.domain.notfctr.MessageTypeConfiguration;
import com.chewy.helper.EmailHelper;
import com.chewy.helper.TemplateManager;
import com.chewy.util.ServiceConstants;
import com.chewy.util.ServiceUtils;
import com.chewy.util.Strings;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.function.FunctionBean;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.inject.Singleton;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function that takes in the json required to build an email and returns the html required to build
 * the email
 */
@Slf4j
@Singleton
@FunctionBean("htmlPreviewHandler")
public class HtmlPreviewServiceImpl implements HtmlPreviewService {

  private final String ATTACHMENT_PARAMETER = "attachment";
  private final EmailHelper emailHelper;
  private final TemplateManager templateManager;

  public HtmlPreviewServiceImpl(EmailHelper emailHelper, TemplateManager templateManager) {
    this.emailHelper = emailHelper;
    this.templateManager = templateManager;
    if (!this.templateManager.localTemplateDirectoryHasFtlFile()) {
      this.templateManager.downloadActiveTemplates();
    }
  }

  public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent awsGatewayRequest) {
    if (awsGatewayRequest.getResource() == null) {
      return handleTemplateUpdate();
    }
    return handlePreviewRequest(awsGatewayRequest);
  }

  public APIGatewayProxyResponseEvent handleTemplateUpdate() {
    templateManager.downloadActiveTemplates();
    return buildResponseEvent();
  }

  public APIGatewayProxyResponseEvent handlePreviewRequest(
      APIGatewayProxyRequestEvent awsGatewayRequest) {
    log.info("Entering HtmlPreviewServiceImpl handlePreviewRequest");

    Context customCtx = new Context();
    ServiceUtils.setRID(awsGatewayRequest, customCtx);
    MDC.put(ServiceConstants.RID_ID_KEY, HtmlPreviewThreadLocal.get().getRid());

    NotificationEvent event = null;
    APIGatewayProxyResponseEvent responseEvent = buildResponseEvent();
    ObjectMapper mapper = new ObjectMapper();
    try {
      event = mapper.readValue(awsGatewayRequest.getBody(), NotificationEvent.class);
    } catch (IOException ex) {
      log.error(
          "failed to read NotificationEvent from awsRequestBody={}",
          Strings.replaceNewLines(awsGatewayRequest.getBody()),
          ex);
      return buildErrorEvent("failed to read NotificationEvent from awsRequestBody");
    }

    MessageTypeConfiguration messageTypeConfiguration = null;
    try {
      MessageTemplateDAO.MessageTemplateSearchCriteria searchCriteria =
          new MessageTemplateDAO.MessageTemplateSearchCriteria(
              event.getType(), true, Channel.EMAIL.toString());
      MessageTemplateDAO messageTemplateDAO = new MessageTemplateDAO();

      messageTypeConfiguration = messageTemplateDAO.getMessageTypeConfiguration(searchCriteria);

      log.info(
          "retrieved the following message type configuration {} for "
              + "message_type={} and channel={}",
          messageTypeConfiguration,
          event.getType(),
          Channel.EMAIL);
    } catch (SQLException ex) {
      log.error(
          "exception retrieving messageTypeConfiguration for message_type={} and channel={}",
          event.getType(),
          Channel.EMAIL,
          ex);
      return buildErrorEvent("exception retrieving messageTypeConfiguration");
    }

    EmailMessageData emailMessageData = null;
    try {
      MessageTemplate template = messageTypeConfiguration.getMessageTemplates().get(0);
      Map<String, List<String>> emailTemplatesGroupedByType =
          emailHelper.groupEmailTemplateNamesByType(template);

      if (isAttachmentPreviewRequest(awsGatewayRequest)) {
        // If email does not have attachment template return error
        if (emailTemplatesGroupedByType.get(ServiceConstants.EMAIL_ATTACHMENT_TEMPLATE_TYPE)
            == null) {
          return buildErrorEvent(
              "email with message_type=" + event.getType() + " does not include an attachment");
        }
        responseEvent.setBody(
            emailHelper.getAttachmentContent(
                emailTemplatesGroupedByType
                    .get(ServiceConstants.EMAIL_ATTACHMENT_TEMPLATE_TYPE)
                    .get(0),
                event));
      } else {
        // Return body content of email
        Map<String, List<String>> bodyEmailTemplates = new HashMap<>();

        List<String> bodyTemplates =
            emailTemplatesGroupedByType.get(ServiceConstants.EMAIL_BODY_TEMPLATE_TYPE);
        if (isPlainTextOnly(awsGatewayRequest)) {
          bodyTemplates.removeIf(bodyTemplate -> bodyTemplate.endsWith("_html.ftl"));
        }

        bodyEmailTemplates.put(
            ServiceConstants.EMAIL_BODY_TEMPLATE_TYPE,
            emailTemplatesGroupedByType.get(ServiceConstants.EMAIL_BODY_TEMPLATE_TYPE));

        emailMessageData =
            emailHelper.buildEmailMessageContent(
                messageTypeConfiguration, bodyEmailTemplates, event);

        responseEvent.setBody(
            Strings.replaceNewLines(
                isPlainTextOnly(awsGatewayRequest)
                    ? emailMessageData.getPlainTextPart()
                    : emailMessageData.getHtmlPart()));
      }
    } catch (Exception ex) {
      log.error("exception composing html preview for event={}", event, ex);
      return buildErrorEvent("exception composing html preview");
    }

    return responseEvent;
  }

  private boolean isPlainTextOnly(APIGatewayProxyRequestEvent awsGatewayRequest) {
    return awsGatewayRequest.getQueryStringParameters() != null
        && awsGatewayRequest.getQueryStringParameters().containsKey("plainText")
        && awsGatewayRequest.getQueryStringParameters().get("plainText").equals("true");
  }

  private APIGatewayProxyResponseEvent buildErrorEvent(String errorMessage) {
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
    apiGatewayProxyResponseEvent.setStatusCode(500);
    apiGatewayProxyResponseEvent.setBody(errorMessage);
    return apiGatewayProxyResponseEvent;
  }

  private APIGatewayProxyResponseEvent buildResponseEvent() {
    Map<String, String> responseHeaders = new HashMap<>();
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
    apiGatewayProxyResponseEvent.setStatusCode(200);
    responseHeaders.put("Content-Type", "text/html");
    responseHeaders.put("Access-Control-Allow-Origin", "*");
    apiGatewayProxyResponseEvent.setHeaders(responseHeaders);
    return apiGatewayProxyResponseEvent;
  }

  private boolean isAttachmentPreviewRequest(APIGatewayProxyRequestEvent awsGatewayRequest) {
    return awsGatewayRequest.getQueryStringParameters() != null
        && awsGatewayRequest.getQueryStringParameters().containsKey(ATTACHMENT_PARAMETER)
        && Boolean.parseBoolean(
            awsGatewayRequest.getQueryStringParameters().get(ATTACHMENT_PARAMETER));
  }
}
