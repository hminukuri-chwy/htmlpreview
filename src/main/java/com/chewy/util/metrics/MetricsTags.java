package com.chewy.util.metrics;//package example.util.metrics;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class MetricsTags {
  // service tags
  public static final String TAG_NOTIFICATION_SERVICE = "notification-service";
  public static final String TAG_PREFERENCE_SERVICE = "preference-service";
  public static final String TAG_KYRIOS_SERVICE = "kyrios-service";
  public static final String TAG_RETURNS_SERVICE = "returns-service";
  public static final String TAG_SEARCH_SERVICE = "search-service";
  public static final String TAG_CONTACT_HISTORY_SERVICE = "contact-history-service";
  public static final String TAG_OMS_SERVICE = "order-service";
  // processor
  public static final String PROCESSOR_TAG_BASE = "processor:";
  public static final String TAG_INBOUND_PROCESSOR = PROCESSOR_TAG_BASE + "inbound";
  public static final String TAG_DELIVERY_PROCESSOR = PROCESSOR_TAG_BASE + "delivery";
  public static final String TAG_PUSH_PROCESSOR = PROCESSOR_TAG_BASE + "push";
  public static final String TAG_EMAIL_PROCESSOR = PROCESSOR_TAG_BASE + "email";
  public static final String TAG_FAX_PROCESSOR = PROCESSOR_TAG_BASE + "fax";
  public static final String TAG_BATCH_PROCESSOR = PROCESSOR_TAG_BASE + "batch";
  public static final String TAG_CONTACT_HISTORY_PROCESSOR = PROCESSOR_TAG_BASE + "contactHistory";
  public static final String TAG_SOURCE_EVENT_DISPATCHER = "source";
  public static final String TAG_VETPORTAL_EVENT_DISPATCHER = "vetPortal";
  public static final String TAG_WALLET_EVENT_DISPATCHER = "wallet";
  public static final String TAG_RX_CANCEL_EVENT_DISPATCHER = "rxCancel";
  public static final String TAG_BACK_IN_STOCK_EVENT_DISPATCHER = "backInStock";
  public static final String TAG_TICKLER_EVENT_DISPATCHER = "tickler";
  public static final String TAG_POST_CHAT_EVENT_DISPATCHER = "postChat";
  public static final String TAG_GIFTCARD_DONATION_EVENT_DISPATCHER = "giftcardDonation";
  public static final String TAG_CS_PLATFORM_EVENT_DISPATCHER = "csPlatform";
  public static final String TAG_RETURNS_EVENT_DISPATCHER = "returns";

  // status tags
  public static final String STATUS_TAG_BASE = "status:";
  public static final String TAG_SUCCESS = STATUS_TAG_BASE + "success";
  public static final String TAG_FAILURE = STATUS_TAG_BASE + "failure";
  // method tags
  public static final String METHOD_TAG_BASE = "method:";
  // misc. tags
  public static final String CHANNEL_TAG_BASE = "channel:";
  public static final String CHANNEL_TAG_UNSUPPORTED = CHANNEL_TAG_BASE + "unsupported";
  public static final String TYPE_TAG_BASE = "type:";
  public static final String VENDOR_TAG_BASE = "vendor:";
  public static final String SOURCE_ID_TAG_BASE = "sourceId:";
  public static final String PREF_KEY_TAG_BASE = "prefKey:";
  public static final String TAG_BRAZE = "braze";
}
