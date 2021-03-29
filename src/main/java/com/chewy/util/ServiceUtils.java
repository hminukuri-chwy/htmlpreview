package com.chewy.util;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.chewy.Context;
import com.chewy.HtmlPreviewThreadLocal;
import com.chewy.domain.aws.AWSGatewayRequest;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Longs;
import io.micronaut.core.annotation.Introspected;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Introspected
public class ServiceUtils {

  /*
  Maintains a mapping of site id to site name/identifier
   */
  private static ImmutableMap<Long, String> siteMap = ImmutableMap.of(
          20L, "ChewyPro", 40L, "Chewy Wholesale Pharmacy");


  public static void setMessageAttributes(Map<String, MessageAttributeValue> msgAttributes, Context context, boolean overrideRid) {
    String rid = null;
    if (msgAttributes != null && msgAttributes.get(ServiceConstants.RID_ID_KEY) != null) {
      if (overrideRid) {
        rid = UUID.randomUUID().toString();
        log.info("Overriding previous rid of {} with new rid={}",
            msgAttributes.get(ServiceConstants.RID_ID_KEY).stringValue(),
            rid
        );
      } else {
        rid = Strings.isBlank(msgAttributes.get(ServiceConstants.RID_ID_KEY).stringValue())
            ? UUID.randomUUID().toString() : msgAttributes.get(ServiceConstants.RID_ID_KEY).stringValue();
      }
    } else {
      rid = UUID.randomUUID().toString();
    }
    String dataFormat = null;
    if (msgAttributes != null && msgAttributes.get(ServiceConstants.DATA_FORMAT_KEY) != null) {
      dataFormat = msgAttributes.get(ServiceConstants.DATA_FORMAT_KEY).stringValue();
    }
    context.setRid(rid);
    context.setDataFormat(dataFormat);
    HtmlPreviewThreadLocal.set(context);
  }

  public static void setMessageAttributes(Map<String, MessageAttributeValue> msgAttributes, Context context) {
    setMessageAttributes(msgAttributes, context, false);
  }

  public static void setRID(APIGatewayProxyRequestEvent request, Context context) {
    String rid = null;
    if (request.getHeaders() != null && request.getHeaders().get(ServiceConstants.RID_ID_KEY_HEADER) != null) {
      rid = Strings.isBlank(request.getHeaders().get(ServiceConstants.RID_ID_KEY_HEADER))
          ? UUID.randomUUID().toString() : request.getHeaders().get(ServiceConstants.RID_ID_KEY_HEADER);
    } else {
      rid = UUID.randomUUID().toString();
    }
    context.setRid(rid);
    HtmlPreviewThreadLocal.set(context);
  }

  public static void setRID(AWSGatewayRequest request, Context context) {
    String rid = null;
    if (request.getHeaders() != null && request.getHeaders().getXCorrelationId() != null) {
      rid = Strings.isBlank(request.getHeaders().getXCorrelationId())
          ? UUID.randomUUID().toString() : request.getHeaders().getXCorrelationId();
    } else {
      rid = UUID.randomUUID().toString();
    }
    context.setRid(rid);
    HtmlPreviewThreadLocal.set(context);
  }

  public static byte[] compressBytes(byte[] dataToCompress) throws IOException {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length);
    try (GZIPOutputStream zipStream = new GZIPOutputStream(byteStream)) {
      zipStream.write(dataToCompress);
    }
    return byteStream.toByteArray();
  }

  // does this do exactly what it implies its checking if the attribute value denotes compressed
  // but its forcing it to be gzip which is potentially not always the case... we can use the
  // gzip format as our standard (as its OMS's) but what if other systems send  us something else
  public static boolean eventIsCompressed(Map<String, MessageAttributeValue> messageAttributes) {
    if (messageAttributes != null && messageAttributes.get(ServiceConstants.DATA_FORMAT_KEY) != null
            && messageAttributes.get(ServiceConstants.DATA_FORMAT_KEY).stringValue() != null) {
      return messageAttributes.get(ServiceConstants.DATA_FORMAT_KEY).stringValue()
              .equals(ServiceConstants.DATA_FORMAT_VALUE_GZIP);
    }
    return false;
  }


  /**
   * take a site id and returns the name of the site if it exists
   * @param siteId id of the site associated with an event
   * @return the name of the site
   */
  public static String getSiteMap(Long siteId) {
    return siteMap.get(siteId);
  }

  /**
   * take a site id as a string and returns the name of the site if it exists
   * @param siteId id of the site associated with an event
   * @return the name of the site
   */
  public static String getSiteMap(String siteId) {
    return getSiteMap(Longs.tryParse(siteId));
  }

}