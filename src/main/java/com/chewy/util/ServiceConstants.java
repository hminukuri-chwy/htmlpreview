package com.chewy.util;

import com.google.common.collect.ImmutableMap;
import io.micronaut.core.annotation.Introspected;

import java.util.Map;

import static com.chewy.domain.MessageTypeName.GC_RECIP;

@Introspected
public class ServiceConstants {

  // aws
  public static final String DEFAULT_AWS_REGION = "us-east-1";
  public static final String AMERICA_NEW_YORK = "America/New_York";
  public static final String SENT_TIMESTAMP_MSG_ATTRIBUTE = "SentTimestamp";


  // message attributes
  public static final String RID_ID_KEY = "X-Request-ID";
  public static final String RID_ID_KEY_HEADER = "xCorrelationId";
  public static final String DATA_FORMAT_KEY = "dataFormat";
  public static final String DATA_FORMAT_VALUE_GZIP = "compressed/gzip";
  public static final String EVENT_TYPE_KEY = "EVENT_TYPE";
  public static final String UNKNOWN_EVENT_TYPE = "UNKNOWN";
  public static final String FIRST_TIME_PROCESSED = "firstTimeProcessed";

  // rest api
  public static final String API_KEY_HEADER = "x-api-key";
  public static final String SCHEME = "https";
  public static final String API_KEY_ENV_VAR = "API_KEY";

  // retry logic for event processing
  public static final int DEFAULT_INBOUND_MESSAGE_RETRY_DELAY = 60;
  public static final String WIZMO_EVENT_RETRY_WINDOW_ENV_VAR = "WIZMO_EVENT_RETRY_WINDOW";
  public static final String SUBSCRIPTION_EVENT_RETRY_WINDOW_ENV_VAR = "SUBSCRIPTION_EVENT_RETRY_WINDOW";
  public static final String INBOUND_MESSAGE_RETRY_WINDOW_ENV_VAR = "INBOUND_MESSAGE_RETRY_WINDOW";
  public static final String CONTACT_HISTORY_RETRY_WINDOW_ENV_VAR = "CONTACT_HISTORY_RETRY_WINDOW";
  public static final Integer DEFAULT_EMAIL_NOTIFICATION_RETRY_DELAY = 120; // 2 mins or make configurable
  public static final Integer DEFAULT_RETURNS_NOTIFICATION_RETRY_DELAY = 120; // 2 mins or make configurable
  public static final Integer DEFAULT_FAX_NOTIFICATION_RETRY_DELAY = 120; // 2 mins or make configurable
  public static final Integer DEFAULT_DELIVERY_EVENT_RETRY_DELAY = 60;
  public static final Integer DEFAULT_SUBSCRIPTION_EVENT_RETRY_DELAY = 120;
  public static final String EMAIL_NOTIFICATION_RETRY_WINDOW_ENV_VAR = "EMAIL_NOTIFICATION_RETRY_WINDOW";
  public static final String RETURNS_NOTIFICATION_RETRY_WINDOW_ENV_VAR = "RETURNS_NOTIFICATION_RETRY_WINDOW";
  public static final String FAX_NOTIFICATION_RETRY_WINDOW_ENV_VAR = "FAX_NOTIFICATION_RETRY_WINDOW";
  public static final int DEFAULT_PUSH_NOTIFICATION_RETRY_DELAY = 120; // 2 mins or make configurable
  public static final String PUSH_NOTIFICATION_REQUIRED_PREF_KEY = "CUSTOMERNO";
  public static final String PUSH_NOTIFICATION_RETRY_WINDOW_ENV_VAR = "PUSH_NOTIFICATION_RETRY_WINDOW";
  public static final int CONTACT_HISTORY_FAILURE_DELAY = 60;
  public static final String SUPPRESS_PUSH_CONTACT_HISTORY_ENV_VAR = "SUPPRESS_PUSH_CONTACT_HISTORY_ENV_VAR";
  public static final String SUPPRESS_PUSH_CONTACT_HISTORY_WRITE = System.getenv(SUPPRESS_PUSH_CONTACT_HISTORY_ENV_VAR);

  // duplicate message sending prevention
  public static final String CONTACT_HISTORY_LOOKBACK_WINDOW_ENV_VAR =
      "CONTACT_HISTORY_LOOKBACK_WINDOW";

  // Source Systems IDs
  public static final int OMS_SOURCE_SYSTEM_ID = 1;
  public static final int CWP_SOURCE_SYSTEM_ID = 5;
  public static final int VET_PORTAL_SOURCE_SYSTEM_ID = 6;
  public static final int AUTOSHIP_SOURCE_SYSTEM_ID = 7;
  public static final int OSVC_SOURCE_SYSTEM_ID = 8;
  public static final int DELIVERY_SOURCE_SYSTEM_ID = 12;
  public static final int PAYMENT_PLATFORM_SOURCE_SYSTEM_ID = 14;
  public static final int RETURNS_SOURCE_SYSTEM_ID = 15;
  public static final int CS_PLATFORM_SOURCE_SYSTEM_ID = 16;
  public static final int TICKLER_SERVICE_SOURCE_SYSTEM_ID = 17;
  public static final int SUBSCRIPTION_SOURCE_SYSTEM_ID = 19;

  //EDI
  public static final String PO_CREATE = "PO_CREATE";
  public static final String PO_MISSING_ACK = "PO_MISSING_ACK";
  public static final String PO_MISSING_APPT= "PO_MISSING_APPT";
  public static final String PO_MISSING_TRACKING = "PO_MISSING_TRACKING";

  //Shelter Emails
  public static final String SHELTER_SIGNUP_APPROVED = "SHELTER_SIGNUP_APPROVED";
  public static final String SHELTER_SIGNUP_INFO_NEEDED = "SHELTER_SIGNUP_INFO_NEEDED";
  public static final String SHELTER_BUSINESS_NEEDS_WORK = "SHELTER_BUSINESS_NEEDS_WORK";
  public static final String SHELTER_BUSINESS_APPROVED = "SHELTER_BUSINESS_APPROVED";
  public static final String SHELTER_GOAL_REACHED = "SHELTER_GOAL_REACHED";


  // Preference Constants
  public static final String NONE_ID_KEY = "NONE";
  public static final String NONE_ID_VALUE = "NONE";
  public static final Long NONE_PREFERENCE_ID = -2L;
  public static final String CUSTOMERNO_SOURCE_KEY = "CUSTOMERNO";
  public static final String ORGNAME_SOURCE_KEY = "ORGNAME";
  public static final String EMAIL_SOURCE_KEY = "EMAIL";
  public static final String ORDERNO_SOURCE_KEY = "ORDERNO";
  public static final String PREFERENCE_CHANNEL_TOGGLE_OFF = "OFF";

  // misc
  public static final String STAGE = "STAGE";



  public static final String TRUE = "true";
  public static final String TEST_SUBSITE_ID = "99999";

  // email
  public static final String EMAIL_SENDER_KEY = "SENDER";
  public static final String DEFAULT_EMAIL_SENDER = "service@chewy.com";
  public static final String RX_EMAIL_SENDER = "rx@chewy.com";
  public static final String EMAIL_BODY_TEMPLATE_TYPE = "BODY";
  public static final String EMAIL_SUBJECT_TEMPLATE_TYPE = "SUBJECT";
  public static final String EMAIL_ATTACHMENT_TEMPLATE_TYPE = "ATTACHMENT";
  public static final Map<String, String> EMAIL_SENDER_MAPPING =
          ImmutableMap.of(GC_RECIP, "no-reply@chewy.com");
  public static final String IS_COMPOSED = "isComposed";

  // User contact
  public static final String USER = "USER";

  //BO Retries
  public static final String BO_LOGIN_RETRIES = "BO_LOGIN_RETRIES";
  public static final String BO_ORDER_RETRIES = "BO_ORDER_RETRIES";
  public static final String SEGMENT_RETRIES = "SEGMENT_RETRIES";

  // Shelter constants
  public static final String ORGANIZATION = "organization";
  public static final String ORGANIZATION_ID = "organizationId";
  public static final String VIRTUAL_GIFTCARD = "virtualGiftcard";

  // Order Constants
  public static final String ORDER_KEY = "order";


}