package com.chewy.helper;

import com.chewy.config.EmailConfig;
import com.chewy.dao.ContactHistoryDAO;
import com.chewy.domain.MessageTypeName;
import com.chewy.domain.email.EmailMessageContent;
import com.chewy.domain.email.EmailMessageData;
import com.chewy.domain.events.NotificationEvent;
import com.chewy.domain.notfctr.MessageTemplate;
import com.chewy.domain.notfctr.MessageTemplateDetail;
import com.chewy.domain.notfctr.MessageTypeConfiguration;
import com.chewy.domain.prefctr.PreferenceDetails;
import com.chewy.exception.NPCErrorCode;
import com.chewy.exception.NPCException;
import com.chewy.service.client.OracleApiClient;
import com.chewy.util.ServiceConstants;
import com.chewy.util.Strings;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import freemarker.template.TemplateException;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Singleton
@AllArgsConstructor(onConstructor = @__(@Inject))
@Introspected
public class EmailHelper {

  public static final String BODY = "body";
  public static final Boolean USE_SENDGRID_REST_API = Boolean.parseBoolean(System.getenv("USE_SENDGRID_REST_API"));
  public static final Boolean USE_SENDGRID_REST_API_SEND_ALL = Boolean.parseBoolean(System.getenv("USE_SENDGRID_REST_API_SEND_ALL"));
  public static final String SEND_GRID_REST_API_LIST = System.getenv("SEND_GRID_REST_API_LIST");
  public static final String NOTIFICATION_METADATA_ATTACHMENTS = "attachments";
  public static final String TMP_ATTACHMENTS = "/tmp/attachments";
  public static final String TOTAL_RETURN_LABELS = "totalReturnLabels";
  // This was to obfuscate the link on the call to ODS, do we need to handle this still if so where
  private static final String PASSWORD_RESET_REQUEST = "PasswordResetRequest";
  private static final String HTML_TEMPLATE_SUFFIX = "_html.ftl";
  private static final String HTML_TEMPLATE_EXTENSION = "ftlh";
  private static final String MAIL_HOST = "mail.host";
  private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
  private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
  private static final String MAIL_SMTP_CONNECT_TIMEOUT = "mail.smtp.connectiontimeout";
  private static final String ATTACHMENT_NAME = "attachmentName";
  private static final String INCIDENT_ID = "incidentId";
  private static final String ATTACHMENT_IDS = "attachmentIds";
  private static final String ATTACHMENT_ID_IMAGE_CONTENT = "attachmentIdImageContent";
  private static final String downloadTemplates = System.getenv("DOWNLOAD_TEMPLATES");
  private static final String domainRegex = "^[a-zA-Z0-9_.+-]+@(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?(chewy|litmus)\\.com$";
  public static final String VENDOR_LABELS_MAP = "vendorLabelsMap";

  private static Set<String> sendGridEventTypeSet = null;
  private static final String WHITELIST_ENABLED = System.getenv("WHITELIST_ENABLED");
  private static final String CHECK_ALREADY_SENT = System.getenv("CHECK_ALREADY_SENT");
  private static final String EVENTS_OMITTED_FROM_CHECK_SENT = System.getenv("EVENTS_OMITTED_FROM_CHECK_SENT");

  private final FreemarkerMessageComposer freemarkerMessageComposer;
  private final EmailConfig emailConfig;
  private final ContactHistoryDAO contactHistoryDAO;
  private final ITextRendererFactory iTextRendererFactory;
  private final OracleApiClient oracleApiClient;
//  private final ReturnsClientHelper returnsClientHelper;

  /**
   * helper to organize multiple message templates by type.
   *
   * @param messageTemplate the message template to process
   * @return
   */

  public Map<String, List<String>> groupEmailTemplateNamesByType(MessageTemplate messageTemplate) {
    Map<String, List<String>> groupedTemplates = new HashMap<>();
    messageTemplate.getTemplateDetails().forEach(messageTemplateDetail -> {
      groupedTemplates.computeIfAbsent(messageTemplateDetail.getType(), k -> new ArrayList<>())
          .add(messageTemplateDetail.getTemplateFileName());
    });
    log.info("GROUPED TEMPLATES: {}", groupedTemplates);
    return groupedTemplates;
  }

  /**
   * Returns a mapping of the ftl template name to the outbound attachment name
   *
   * @param messageTemplate
   * @param notificationMetaData
   * @return
   */
  public Map<String, String> mapTemplatesToAttachmentNames(MessageTemplate messageTemplate, Map<String, Object> notificationMetaData) {
    Map<String, String> attachmentNames = new HashMap<>();
    Map<String, String> attachmentOverrideNamesMap = (HashMap<String, String>) notificationMetaData.getOrDefault(NOTIFICATION_METADATA_ATTACHMENTS, new HashMap<>());
    for (MessageTemplateDetail messageTemplateDetail : messageTemplate.getTemplateDetails()) {
      if (ServiceConstants.EMAIL_ATTACHMENT_TEMPLATE_TYPE.equals(messageTemplateDetail.getType())) {
        if (attachmentOverrideNamesMap.containsKey(messageTemplateDetail.getOutboundAttachmentName())) {
          attachmentNames.put(messageTemplateDetail.getTemplateFileName(), attachmentOverrideNamesMap.get(messageTemplateDetail.getOutboundAttachmentName()));
        } else {
          attachmentNames.put(messageTemplateDetail.getTemplateFileName(),messageTemplateDetail.getOutboundAttachmentName());
        }
      }
    }
    return attachmentNames;
  }

  public EmailMessageData buildEmailMessageContent(MessageTypeConfiguration config,
                                                   Map<String, List<String>> emailTemplates,
                                                   NotificationEvent event) throws NPCException {

    log.info("building email message content for message_type={} and recipient={}",
        event.getType(), event.getRecipient());

    List<String> templateNamesForEmailContent =
        emailTemplates.get(ServiceConstants.EMAIL_BODY_TEMPLATE_TYPE);
    List<String> subjectTemplates =
        emailTemplates.getOrDefault(
            ServiceConstants.EMAIL_SUBJECT_TEMPLATE_TYPE, ImmutableList.of());
    List<String> attachmentTemplates =
        emailTemplates.get(ServiceConstants.EMAIL_ATTACHMENT_TEMPLATE_TYPE);

    EmailMessageData emailMessageData = new EmailMessageData();
    try {
      log.info("composing email message body with the following templates {}",
          templateNamesForEmailContent);
      EmailMessageContent emailMessageContent =
          processEmailTemplateData(templateNamesForEmailContent, event.getNotificationMetaData(), event.getIsComposed());
      log.info("successfully composed email message body");
      emailMessageData.setMsgType(config.getMessageType().getMessageTypeName());

      // To allow for dynamic subject lines or no subject move subject information into
      // template file as opposed to db column. By convention there should be 1 and only 1
      // template for the subject
      if (!subjectTemplates.isEmpty()) {
        String subject = freemarkerMessageComposer.composeMessage(
            subjectTemplates.get(0), event.getNotificationMetaData());
        emailMessageData.setSubject(subject);
        // since subject is now dynamic update the config
        config.getMessageType().setSubject(subject);
      }

      // TODO - re-add this functionality for multiple attachment support and replace functionality below
      /*
      if (attachmentTemplates!= null && !attachmentTemplates.isEmpty()) {
        Map<String,String> templateNames = mapTemplatesToAttachmentNames(config.getMessageTemplates().get(0), event.getNotificationMetaData());
        emailMessageData.setAttachmentPath(createAttachments(event, templateNames));
      }
      */
      if (attachmentTemplates != null && !attachmentTemplates.isEmpty()) {
        String attachmentPath = "";
        String templateFileName = "";
        for (MessageTemplateDetail messageTemplateDetail : config.getMessageTemplates().get(0).getTemplateDetails()) {
          if (ServiceConstants.EMAIL_ATTACHMENT_TEMPLATE_TYPE.equals(messageTemplateDetail.getType())) {
            templateFileName = messageTemplateDetail.getTemplateFileName();
            attachmentPath = messageTemplateDetail.getOutboundAttachmentName();
          }
        }
        if (event.getNotificationMetaData().containsKey(ATTACHMENT_NAME)) {
          // Override default attachment name
          attachmentPath = event.getNotificationMetaData().get(ATTACHMENT_NAME).toString();
        }
        Map<String, String> attachmentMap = new HashMap<>();
        attachmentMap.put(templateFileName, attachmentPath);

        // TODO - move this logic to the PostChatEventDispatcher
        // Image content should be uploaded to S3 and presigned url list should be provided in the event to
        // be referenced in the templates
        if (MessageTypeName.VET_HEALTH_POST_CHAT.equals(event.getType()) && event.getNotificationMetaData().get(ATTACHMENT_IDS) != null) {
          event.getNotificationMetaData().put(ATTACHMENT_ID_IMAGE_CONTENT, oracleApiClient.getAttachmentContent(
              (Integer)event.getNotificationMetaData().get(INCIDENT_ID),
              (List<Integer>)event.getNotificationMetaData().get(ATTACHMENT_IDS))
          );
        }

        emailMessageData.setAttachmentPath(createAttachments(event, attachmentMap));
      }

      emailMessageData.setPlainTextPart(emailMessageContent.getPlainTextPart());
      emailMessageData.setHtmlPart(emailMessageContent.getHtmlPart());
      emailMessageData.setRecipient(event.getRecipient());
      emailMessageData.setRecipientCC(event.getRecipientCC());
      emailMessageData.setRecipientBCC(event.getRecipientBCC());
      emailMessageData.setEventType(event.getType());
      emailMessageData.setSourceSystemId(event.getSourceSystemId());
      // message_sender added to database now use that as default and the existing overrides
      // as a fall back.  once migration of functionality to database is validated
      // we can remove the fall back condition
      if (Strings.isNotBlank(config.getMessageTemplates().get(0).getSender())) {
        emailMessageData.setSender(config.getMessageTemplates().get(0).getSender());
      } else if (Strings.isNotBlank((String) event.getNotificationMetaData()
          .get(ServiceConstants.EMAIL_SENDER_KEY))) {
        emailMessageData.setSender(
            (String) event.getNotificationMetaData().get(ServiceConstants.EMAIL_SENDER_KEY));
      }
      emailMessageData.setLabel(config.getMessageTemplates().get(0).getLabel());
      // TODO add cc bcc here
      emailMessageData.setTimeReceived(event.getFirstProcessedTimeStamp());
      return emailMessageData;
    } catch (Exception ex) {
      if (attachmentTemplates != null && !attachmentTemplates.isEmpty()) {
        AttachmentHelper.cleanupAttachmentDirectories(emailMessageData.getAttachmentPath());
      }
      throw new NPCException(NPCErrorCode.EMAIL_COMPOSE_FAILURE,
          "failed to compose email message body for message_type=" + event.getType()
              + " to recipient=" + event.getRecipient() + " using templates "
              + templateNamesForEmailContent,
          ex
      );
    }
  }

  public String getAttachmentContent(String attachmentTemplateKey, NotificationEvent event) throws IOException, TemplateException{
    return freemarkerMessageComposer.composeMessage(attachmentTemplateKey, event.getNotificationMetaData());
  }


  public boolean checkEventAlreadySent(PreferenceDetails preferenceDetails, MessageTypeConfiguration msgTypeConfig, String rid) {
    String messageType = msgTypeConfig.getMessageType().getMessageTypeName();
    if (!shouldCheckAlreadySent() || eventsOmittedFromCheckSent().contains(messageType)) {
      return false;
    }
    try {
      log.info("checking if the message_type={} has been sent in the last {} minutes for {}={}",
          messageType,
          System.getenv(ServiceConstants.CONTACT_HISTORY_LOOKBACK_WINDOW_ENV_VAR),
          preferenceDetails.getIdKey(),
          preferenceDetails.getIdValue()
      );
      return contactHistoryDAO.checkExistingContactHistory(preferenceDetails.getIdKey(), preferenceDetails.getIdValue(),
          msgTypeConfig.getMessageTemplates().get(0).getId(), rid);
    } catch (SQLException ex) {
      log.error("failed to successfully check if event already sent in the last {} minutes",
          System.getenv(ServiceConstants.CONTACT_HISTORY_LOOKBACK_WINDOW_ENV_VAR), ex);
      return false;
    }
  }

  // TODO we should make sure the usage of template here is appropriate since they can expired.  do we need to change
  // the logic to be something more like get any template ids associated with the message type active or inactive,
  // then have an "in" query using all of those template ids to make sure we truely capture all time.
  public boolean checkEventEverSent(PreferenceDetails preferenceDetails, MessageTypeConfiguration msgTypeConfig) {
    try {
      log.info("checking if the message_type={} has ever been sent for message template {} with id={} for {}={}",
          msgTypeConfig.getMessageType().getMessageTypeName(),
          msgTypeConfig.getMessageTemplates().get(0).getDescription(),
          msgTypeConfig.getMessageTemplates().get(0).getId(),
          preferenceDetails.getIdKey(),
          preferenceDetails.getIdValue());
      return contactHistoryDAO.checkIfEverSent(preferenceDetails.getIdKey(), preferenceDetails.getIdValue(),
          msgTypeConfig.getMessageTemplates().get(0).getId());
    } catch (SQLException ex) {
      log.error("failed to successfully check if event has ever been sent", ex);
      return false;
    }
  }

  public boolean isMultipart(EmailMessageData msgData) {
    return Strings.isNotBlank(msgData.getPlainTextPart())
        && Strings.isNotBlank(msgData.getHtmlPart());
  }

  public void addInternetAddress(MimeMessage message, Message.RecipientType recipientType,
                                 String currentRecipient) throws MessagingException {
    if (Strings.isNotBlank(currentRecipient)) {
      message.addRecipients(recipientType, formatMultipleRecipients(currentRecipient));
    }
  }

  /**
   * Allow for sending to multiple recipients.  Should work for single recipient as well
   * as it will just create a list of one.  Also support either the comma or semi colon
   * delimiter
   *
   * @param currentRecipient
   * @return
   * @throws AddressException
   */
  private InternetAddress[] formatMultipleRecipients(String currentRecipient) throws AddressException {
    String[] recipientList = parseRecipients(currentRecipient);
    InternetAddress[] recipientAddresses = new InternetAddress[recipientList.length];
    for (int i = 0; i < recipientList.length; i++) {
      recipientAddresses[i] = new InternetAddress(recipientList[i]);
      log.info("Successfully added internet address for {}", recipientList[i]);
    }
    return recipientAddresses;
  }

  /**
   * Allow for sending to multiple recipients.  Should work for single recipient as well
   * as it will just create a list of one. Parses recipients by either comma or semi colon
   * delimiter
   *
   * @param currentRecipient
   * @return
   */
  public String[] parseRecipients(String currentRecipient) {
    if (currentRecipient == null) {
      return new String[0];
    }
    if (currentRecipient.contains(";")) {
      return currentRecipient.split(";");
    } else {
      //must be a comma
      return currentRecipient.split(",");
    }
  }

  private EmailMessageContent processEmailTemplateData(List<String> emailBodyTemplates,
                                                       Map<String, Object> messageParams, Boolean composed) throws Exception {

    EmailMessageContent emailMessageContent = new EmailMessageContent();

    for (String templateName : emailBodyTemplates) {
      if(composed == null || !composed) {
        String messagePart =
            freemarkerMessageComposer.composeMessage(templateName, messageParams);
        if (isHtmlTemplate(templateName)) {
          emailMessageContent.setHtmlPart(messagePart);
        } else {
          emailMessageContent.setPlainTextPart(messagePart);
        }
      } else {
        emailMessageContent.setPlainTextPart((String) messageParams.get(BODY));
      }

    }

    return emailMessageContent;
  }

  private Set<String> createAttachments(NotificationEvent event, Map<String, String> attachmentTemplateNamesMap) throws Exception {
    Set<String> attachmentPaths = new HashSet<>();
    AttachmentHelper.createAttachmentParentDirectory();
    for (Map.Entry<String, String> attachmentTemplateEntry : attachmentTemplateNamesMap.entrySet()) {
      String attachmentPath = AttachmentHelper.buildAttachmentPath(attachmentTemplateEntry.getValue());
      log.info("Processing attachment_template_entry={} with attachment_path={}", attachmentTemplateEntry, attachmentPath);
      File outputFile = new File(attachmentPath);
      if (outputFile.exists()) {
        log.info("attachment file directory already exists");
      } else if (!outputFile.getParentFile().mkdirs()) {
        throw new NPCException(NPCErrorCode.UNKNOWN, "unable to create attachments child file directory");
      }
      outputFile.createNewFile();
      attachmentPaths.add(attachmentPath);
      String attachmentContents =
          freemarkerMessageComposer.composeMessage(attachmentTemplateEntry.getKey(), event.getNotificationMetaData());
      try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
        iTextRendererFactory.returnITextRenderer().setDocumentFromString(attachmentContents, "");
        iTextRendererFactory.returnITextRenderer().setDocumentFromString(attachmentContents);
        iTextRendererFactory.returnITextRenderer().layout();
        iTextRendererFactory.returnITextRenderer().createPDF(outputStream);
      }
    }
    log.info("Saved attachments in the following paths: {}", attachmentPaths);
    return attachmentPaths;
  }


  /**
   * We know if a template is html based on either the naming convention or file extension
   *
   * @param templateName name of the template to check
   * @return if its an html template or not
   */
  private boolean isHtmlTemplate(String templateName) {
    return templateName.endsWith(HTML_TEMPLATE_SUFFIX) || templateName.endsWith(HTML_TEMPLATE_EXTENSION);
  }

  public Session getSession(Properties properties, String username, String password) {
    Authenticator authenticator = new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        if (username != null) {
          return new PasswordAuthentication(username, password);
        }
        return null;
      }
    };

    return Session.getDefaultInstance(properties, authenticator);
  }

  public Properties getProperties() {
    Properties props = new Properties();
    props.setProperty(MAIL_TRANSPORT_PROTOCOL, emailConfig.getProtocol());
    props.setProperty(MAIL_HOST, emailConfig.getHost());

    if (Strings.isNotBlank(emailConfig.getConnectTimeout())) {
      props.setProperty(MAIL_SMTP_CONNECT_TIMEOUT, emailConfig.getConnectTimeout());
    }
    if (Strings.isNotBlank(emailConfig.getSmtpTimeout())) {
      props.setProperty(MAIL_SMTP_TIMEOUT, emailConfig.getSmtpTimeout());
    }

    String username = "";
    String password = "";
    if (username != null) {
      Preconditions.checkState(password != null,
          "transport username is set, but password is not set");
      props.setProperty(MAIL_SMTP_AUTH, emailConfig.getAuth());
    }
    return props;
  }

  public boolean shouldDownloadTemplates() {
    return downloadTemplates.equals(ServiceConstants.TRUE);
  }

  public boolean useRestApi(String type) {
    if (USE_SENDGRID_REST_API_SEND_ALL) {
      return true;
    } else if (USE_SENDGRID_REST_API) {
      if(sendGridEventTypeSet == null) {
        sendGridEventTypeSet = buildUseSendGridApiSet();
      }
      return sendGridEventTypeSet.contains(type);
    }
    return false;
  }

  public boolean shouldCheckAlreadySent() {
    return Boolean.parseBoolean(CHECK_ALREADY_SENT);
  }

  public Set<String> eventsOmittedFromCheckSent() {
    return (EVENTS_OMITTED_FROM_CHECK_SENT != null && !EVENTS_OMITTED_FROM_CHECK_SENT.isEmpty()) ?
        new HashSet<>(Arrays.asList(EVENTS_OMITTED_FROM_CHECK_SENT.split(","))) : Collections.emptySet();
  }

  private static ImmutableSet<String> buildUseSendGridApiSet() {
    ImmutableSet.Builder<String> sendGridSet = ImmutableSet.builder();
    String[] sendGridArray = EmailHelper.SEND_GRID_REST_API_LIST.split(",");
    for(String eventType : sendGridArray) {
      sendGridSet.add(eventType);
    }
    return sendGridSet.build();
  }

  /**
   * Method filters out bcc and cc recipients that are not on the white list and checks if recipients are valid
   * @param messageData email message data containing recipient info
   * @return true if 'to' recipients are white listed and false otherwise
   */
  private boolean whiteListRecipients(EmailMessageData messageData) {
    String[] toRecip = parseRecipients(messageData.getRecipient());
    String[] bccRecip = parseRecipients(messageData.getRecipientBCC());
    String[] ccRecip = parseRecipients(messageData.getRecipientCC());
    if(toRecip.length == 0) {
      return false;
    }
    for(String recip : toRecip) {
      if (isInvalidDomain(recip)) {
        return false;
      }
    }
    for(String recip : bccRecip) {
      if (isInvalidDomain(recip)) {
        messageData.setRecipientBCC(null);
      }
    }
    for(String recip : ccRecip) {
      if (isInvalidDomain(recip)) {
        messageData.setRecipientCC(null);
      }
    }
    return true;
  }

  private boolean isInvalidDomain(String recipient) {
    return !recipient.matches(domainRegex);
  }

  public boolean isWhiteListed(EmailMessageData messageData) {
    if (whiteListEnabled()) {
      return whiteListRecipients(messageData);
    } else {
      return true;
    }
  }

  private boolean whiteListEnabled() {
    return Boolean.parseBoolean(WHITELIST_ENABLED);
  }


  // calculate the difference in seconds between receivedTime and current time
  public Long calculateProcessingTimeInSeconds(Long receivedTime) {
    return receivedTime == null ? null : ((System.currentTimeMillis() - receivedTime) / 1000);
  }

  public static boolean containsTemplates(MessageTypeConfiguration msgTypeConfig) {
    for (MessageTemplate template : msgTypeConfig.getMessageTemplates()) {
      for (MessageTemplateDetail detail : template.getTemplateDetails()) {
        String templateFileName = detail.getTemplateFileName();
        if (Strings.isNotBlank(templateFileName)
            && templateFileName.endsWith(".ftl") || templateFileName.endsWith(".ftlh")) {
          return true;
        }
      }
    }
    return false;
  }

//  public void addReturnLabelsInfo(OutboundNotificationEvent event) throws NPCException {
//    List<ReturnLabelDTO> returnsResponseList = returnsClientHelper.retrieveReturnLabels(event.getReturnId());
//    event.setReturnLabels(returnsResponseList.stream().map(ReturnLabelDTO::getLabel).collect(Collectors.toList()));
//    if(!returnsResponseList.isEmpty()) {
//        Map<String, Integer> vendorLabelsMap = new HashMap<>();
//        returnsResponseList.forEach(returnLabelDTO -> {
//          String vendorName = returnLabelDTO.getDestination().getVendorName();
//          vendorLabelsMap.putIfAbsent(vendorName, 0);
//          vendorLabelsMap.computeIfPresent(vendorName, (k, v) -> v + 1);
//        });
//        event.getNotificationMetaData().put(TOTAL_RETURN_LABELS, returnsResponseList.size());
//        event.getNotificationMetaData().put(VENDOR_LABELS_MAP, vendorLabelsMap);
//    }
//  }
  /**
   * Returns the appropriate sender for the given email
   * @param messageData
   * @return
   */
  public String getSender(EmailMessageData messageData) {
    if (Strings.isNotBlank(messageData.getSender())) {
      return messageData.getSender();
    } else {
      return ServiceConstants.EMAIL_SENDER_MAPPING
          .getOrDefault(messageData.getEventType(), ServiceConstants.DEFAULT_EMAIL_SENDER);
    }
  }

}
