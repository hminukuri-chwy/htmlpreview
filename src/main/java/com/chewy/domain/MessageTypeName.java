package com.chewy.domain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class MessageTypeName {

  // OSVC
  public static final String BACK_IN_STOCK = "BACK_IN_STOCK";
  public static final String VET_PORTAL = "VET_PORTAL";
  public static final String VET_HEALTH_POST_CHAT = "VET_HEALTH_POST_CHAT";
  public static final String PBC_REGISTER = "PBC_REGISTER";

  // Payments
  public static final String PAYMENT_ADDED = "USER_PAYMENT_ADDED";

  // WIZMO
  public static final String DELIVERED = "DELIVERED";
  public static final String DONATION_DELIVERED = "DONATION_DELIVERED";
  public static final String EDDMISS = "EDDMISS";

  //Chewy Pro
  public static final String CHEWY_PRO_WELCOME = "PRO_WELCOME";
  public static final String CHEWY_PRO_REJECTION = "PRO_REJECTION";

  //VET PET HEALTH APP
  public static final String VET_HEALTH_APPT_CONFIRMATION = "VET_HEALTH_APPT_CONFIRMATION";
  public static final String VET_HEALTH_APPT_REMINDER = "VET_HEALTH_APPT_REMINDER";
  public static final String VET_HEALTH_APPT_CANCEL_CUST = "VET_HEALTH_APPT_CANCEL_CUST";
  public static final String VET_HEALTH_APPT_CANCEL_CHEWY = "VET_HEALTH_APPT_CANCEL_CHEWY";
  public static final String VET_HEALTH_APPT_MISSED = "VET_HEALTH_APPT_MISSED";
  public static final String VET_HEALTH_LEAD_GEN = "VET_HEALTH_LEAD_GEN";

  // HMS
  public static final String HMS_DECLINED = "HMS_DECLINED";
  public static final String HMS_ITEM_ON_HOLD = "HMS_ITEM_ON_HOLD";
  public static final String HMS_CANCEL_NOTICE = "HMS_CANCEL_NOTICE";
  public static final String HMS_RX_ORDER_UPDATE = "HMS_RX_ORDER_UPDATE";
  public static final String HMS_RX_APPROVAL_NEEDED = "HMS_RX_APPROVAL_NEEDED";
  public static final String HMS_RX_APPROVAL_NEEDED_SKU = "HMS_RX_APPROVAL_NEEDED_SKU";
  public static final String HMS_MAIL_REMINDER = "HMS_MAIL_REMINDER";
  public static final String HMS_ORIGINAL_RX_NEEDED = "HMS_ORIGINAL_RX_NEEDED";
  public static final String HMS_REPLACEMENT_RETURN = "HMS_REPLACEMENT_RETURN";
  public static final String HMS_REPLACEMENT_NO_RETURN = "HMS_REPLACEMENT_NO_RETURN";
  public static final String HMS_PRESCRIPTION_RECEIVED = "HMS_PRESCRIPTION_RECEIVED";
  public static final String HMS_TEMP_OOS = "HMS_TEMP_OOS";
  public static final String HMS_RX_APPROVAL_NEEDED_COMPOUND = "HMS_RX_APPROVAL_NEEDED_COMPOUND";
  public static final String HMS_BLANK_TEMPLATE = "HMS_BLANK_TEMPLATE";

  // EDI
  public static final String PO_EDI_MISSING_DOC = "PO_EDI_MISSING_DOC";
  public static final String PO_EDI_MISSING_INFO = "PO_EDI_MISSING_INFO";
  public static final String PO_EDI_REJECTED = "PO_EDI_REJECTED";
  public static final String RX_AUTO_CANCEL_WARN_DAY3 = "RX_AUTO_CANCEL_WARN_DAY3";
  public static final String RX_AUTO_CANCEL_WARN_DAY5 = "RX_AUTO_CANCEL_WARN_DAY5";
  public static final String RX_AUTO_CANCEL_WARN_DAY7 = "RX_AUTO_CANCEL_WARN_DAY7";
  public static final String RX_AUTO_CANCEL = "RX_AUTO_CANCEL";

  // Order Item Changed Subtype Events
  public static final String REMOVALS = "BACKORDER_REMOVALS";
  public static final String SUBSTITUTES = "BACKORDER_SUBSTITUTES";
  public static final String SUBSTITUTES_AND_REMOVALS = "BACKORDER_SUBSTITUTES_AND_REMOVALS";
  public static final String ORDER_CANCELED_RESTRICTED_PRODUCTS = "ORDER_CANCELED_RESTRICTED_PRODUCTS";
  public static final String ORDER_ITEM_CANCELED_RESTRICTED_PRODUCTS = "ORDER_ITEM_CANCELED_RESTRICTED_PRODUCTS";
  public static final String ORDER_ITEM_CANCELED_BACKORDER = "ORDER_ITEM_CANCELED_BACKORDER";
  public static final String TYLEES = "TYLEES_TRANSITION";

  // Release Ship Notify Types
  public static final String ORDER_RELEASE_SHIPPED_SHIP_COMPLETE = "ORSN_SC";
  public static final String ORDER_RELEASE_SHIPPED_PARTIAL_SHIP = "ORSN_PS";
  public static final String ORDER_RELEASE_SHIPPED_SHELTER = "ORSN_SHELTER";
  public static final String ORDER_RELEASE_SHIPPED_BATCH = "ORDER_RELEASE_SHIPPED_BATCH";

  //Order delay Notif Types
  public static final String ON_TIME_SHIP_DELAY = "ON_TIME_SHIP_DELAY";
  public static final String ORDER_NOT_SHIPPED = "ORDER_NOT_SHIPPED";
  public static final String ORDER_AGED_DELAY = "ORDER_AGED_DELAY";

  // Order Events
  public static final String MULTIPLE_ORDERS = "MULTIPLE_ORDERS";
  public static final String ORDER_PAYMENT_DECLINED_BATCH = "ORDER_PAYMENT_DECLINED_BATCH";
  public static final String ORDER_CANCELED_BATCH = "ORDER_CANCELED_BATCH";


  // Autoship Events
  public static final String AUTOSHIP_CREATED = "AUTOSHIP_CREATED";
  public static final String AUTOSHIP_CHILD_ORDER_CREATED = "AUTOSHIP_CHILD_ORDER_CREATED";

  // Autoship Upcoming event types
  public static final String AUTOSHIP_UPCOMING_PRECURSOR = "AUTOSHIP_UPCOMING_PRECURSOR";
  public static final String AUTOSHIP_UPCOMING = "AUTOSHIP_UPCOMING";
  public static final String AUTOSHIP_UPCOMING_OUT_OF_STOCK = "AUTOSHIP_UPCOMING_OOS";
  public static final String AUTOSHIP_UPCOMING_ZIPCODE_DELAY = "AUTOSHIP_UPCOMING_ZIP";

  // Fax batch Types
  public static final String PETSCRIPTIONS_DEMO = "PETSCRIPTIONS_DEMO";


  // UI Ticklers
  public static final String ORDER_CANCELED_BACKORDER = "BACKORDER_SINGLE_ITEM_NOTICE_CANCELLATION";
  public static final String ORDER_CANCELED_MISSING_RX = "VD_CANCEL_RX_ONLY";
  public static final String FRAUD_AVS_MISMATCH = "FRAUD_AVS_MISMATCH";
  public static final String FRAUD_GENERIC = "FRAUD_GENERIC";
  public static final String FRAUD_SUSPECTED_NEW_CUST_PROMO = "FRAUD_SUSPECTED_NEW_CUST_PROMO";
  public static final String FRAUD_SUSPECTED_NOT_DIST = "FRAUD_SUSPECTED_NOT_DIST";
  public static final String INV_CHECK_FAIL_CNCL_DISCONTINUED = "INV_CHECK_FAIL_CNCL_DISCONTINUED";
  public static final String INV_CHECK_FAIL_WARN_DISCONTINUED = "INV_CHECK_FAIL_WARN_DISCONTINUED";
  public static final String INV_CHECK_FAIL_SUB_DISCONTINUED = "INV_CHECK_FAIL_SUB_DISCONTINUED";
  public static final String OOS_48HR_NOTICE = "OOS_48HR_NOTICE";
  public static final String OOS_NOSUBS_AVAILABLE = "OOS_NOSUBS_AVAILABLE";
  public static final String OOS_SUBS_AVAILABLE_SINGLE = "OOS_SUBS_AVAILABLE_SINGLE";
  public static final String OOS_SUBS_AVAILABLE_MULTI = "OOS_SUBS_AVAILABLE_MULTI";
  public static final String OOS_CROSS_SCENARIO = "OOS_CROSS_SCENARIO";
  public static final String OOS_SPECIAL_PROMO_MULTI = "OOS_SPECIAL_PROMO_MULTI";
  public static final String OOS_SPECIAL_PROMO_48HR = "OOS_SPECIAL_PROMO_48HR";
  public static final String OOS_SPECIAL_PROMO_X_SCENARIO = "OOS_SPECIAL_PROMO_X_SCENARIO";
  public static final String ORDER_CREATED = "ORDER_CREATED";
  public static final String ORDER_INVOICE = "ORDER_INVOICE";
  public static final String ORDER_CANCELED = "ORDER_CANCELED";
  public static final String BACKORDER_AUTOMATION_REMINDER_48 = "BACKORDER_AUTOMATION_REMINDER_48";
  public static final String BACKORDER_AUTOMATION_REMINDER_24 = "BACKORDER_AUTOMATION_REMINDER_24";
  public static final String BACKORDER_TICKLER_REMINDER_24 = "BACKORDER_TICKLER_REMINDER_24";
  public static final String FRAUD_TICKLER_REMINDER_24 = "FRAUD_TICKLER_REMINDER_24";
  public static final String MULTIPLE_ORDER_TICKLER_REMINDER_24 = "MULTIPLE_ORDER_TICKLER_REMINDER_24";

  // Gift Cards
  public static final String GC_RECIP = "GC_RECIP";
  public static final String GIFTCARD_REMINDER = "GIFTCARD_REMINDER";
  public static final String GC_RECIP_DONATION = "GC_RECIP_DONATION";

  //Returns
  public static final String RETURN_LABELS_GENERATED = "RETURN_LABELS_GENERATED";
  public static final String RETURN_LABELS_GENERATED_DROPSHIP = "RETURN_LABELS_GENERATED_DROPSHIP";
  public static final String RETURN_CREATED = "RETURN_CREATED";
  public static final String SHELTER_TAX_DONATION = "SHELTER_TAX_DONATION";



}
