package com.chewy.dao;

import com.chewy.domain.events.ContactHistoryRecord;
import com.chewy.domain.notfctr.ChannelMetadata;
import com.chewy.domain.notfctr.ContactHistoryRecordV1;
import com.chewy.helper.DatabaseHelper;
import com.chewy.requests.ContactHistoryResponse;
import com.chewy.requests.ContactHistoryWrapper;
import com.chewy.util.ServiceConstants;
import com.chewy.util.Strings;
import com.chewy.requests.ContactHistoryRequest;
import com.chewy.util.QueryUtils;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
@Introspected
public class ContactHistoryDAO {
  private static final Long DEFAULT_UNKNOWN_ID = -1L;
  private static final String DEFAULT_UNKNOWN = "UNKNOWN";
  private static final String RETRY_WINDOW = "RETRY_WINDOW";

  private final String CHECK_EXISTING_CONTACT_HISTORY = "SELECT 1 FROM notfctr.contact_history ch " +
      "WHERE ch.source_entity = ? " +
      "AND ch.source_entity_value = ? " +
      "AND ch.message_template_id = ? " +
      "AND ch.source_guid = ? ";

  private final String CHECK_EVER_SENT = "SELECT 1 FROM notfctr.contact_history ch " +
      "WHERE ch.source_entity = ? " +
      "AND ch.source_entity_value = ? " +
      "AND ch.message_template_id = ?;";

  private final String INSERT_CONTACT_HISTORY_QUERY =
      "INSERT INTO notfctr.contact_history" +
          "(" +
          "contact_history_id, " +
          "source_system_id, " +
          "source_key_id, " +
          "source_entity, " +
          "source_entity_value, " +
          "message_template_id, " +
          "message_desc, " +
          "message_subject, " +
          "message_body, " +
          "source_guid, " +
          "email_full, " +
          "phone_full, " +
          "pref_type_id, " +
          "pref_type_nm, " +
          "pref_act_type_id, " +
          "pref_act_type_nm, " +
          "vendor_nm, " +
          "order_id, " +
          "sent_device_id, " +
          "sent_dt, " +
          "sent_ind, " +
          "message_identifier," + // make sure this is right
          "create_dt, " +
          "create_by, " +
          "update_dt, " +
          "update_by," +
          "subscription_id," +
          "member_id," +
          "fax_number," +
          "return_id," +
          "out_of_stock_part_numbers" +
          ")" +
          "VALUES " +
          "(nextval('notfctr.contact_history_id_seq')," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "CURRENT_TIMESTAMP, " +
          "CURRENT_USER, " +
          "CURRENT_TIMESTAMP, " +
          "CURRENT_USER," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?" +
          ")";

  private final String INSERT_CONTACT_HISTORY_QUERY_FOR_BATCH =
      "INSERT INTO notfctr.contact_history" +
          "(" +
          "contact_history_id, " +
          "source_system_id, " +
          "source_key_id, " +
          "source_entity, " +
          "source_entity_value, " +
          "message_template_id, " +
          "message_desc, " +
          "message_subject, " +
          "message_body, " +
          "source_guid, " +
          "email_full, " +
          "phone_full, " +
          "pref_type_id, " +
          "pref_type_nm, " +
          "pref_act_type_id, " +
          "pref_act_type_nm, " +
          "vendor_nm, " +
          "order_id, " +
          "sent_device_id, " +
          "sent_dt, " +
          "sent_ind, " +
          "message_identifier," +
          "create_dt, " +
          "create_by, " +
          "update_dt, " +
          "update_by," +
          "subscription_id," +
          "member_id," +
          "fax_number," +
          "return_id," +
          "out_of_stock_part_numbers" +
          ")" +
          "VALUES " +
          "(nextval('notfctr.contact_history_id_seq')," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "(SELECT pref_act_type_id FROM prefctr.pref_action_type WHERE pref_act_type_nm = ?)," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?," +
          "CURRENT_TIMESTAMP, " +
          "CURRENT_USER, " +
          "CURRENT_TIMESTAMP, " +
          "CURRENT_USER," +
          "?," +
          "?," +
          "?," +
          "?," +
          "?" +
          ")";

  /*
  Updated insert endpoint to support processing write contact history requests from NPC.  We could explore
  having this support batch so that we can pull groups of messages off the sqs queue and bulk process them
   */
  public Long insertContactHistory(ContactHistoryRecord contactHistoryRecord)
      throws SQLException {

    try (Connection conn = DriverManager.getConnection(DatabaseHelper.getURL());
         PreparedStatement preparedStatement =
             conn.prepareStatement(INSERT_CONTACT_HISTORY_QUERY,
                 new String[]{"contact_history_id"})) {
      return insertContactHistory(preparedStatement, contactHistoryRecord);
    }
  }

  private Long insertContactHistory(
      PreparedStatement preparedStatement, ContactHistoryRecord contactHistoryRecord)
      throws SQLException {

    preparedStatement.setInt(1, contactHistoryRecord.getSourceSystemId() == null
        ? -1 : contactHistoryRecord.getSourceSystemId());
    //*********************************************
    // TODO make sure as part of the deployment of the refactor we have a schema
    // change to drop the constraint on source key id and source system ideally it should
    // be removed from the V1 migration script we will run against the new DB infrastructure
    //*********************************************

    preparedStatement.setLong(2, contactHistoryRecord.getSourceKeyId() == null
        ? -1 : contactHistoryRecord.getSourceKeyId());
    // TODO think about being more defensive here to avoid npes if pref details are ever not required
    preparedStatement.setString(3, contactHistoryRecord.getSourceEntity());
    preparedStatement.setString(4, contactHistoryRecord.getSourceEntityValue());
    preparedStatement.setLong(5, contactHistoryRecord.getMsgTemplateId() == null
        ? DEFAULT_UNKNOWN_ID : contactHistoryRecord.getMsgTemplateId());
    preparedStatement.setString(6, contactHistoryRecord.getMsgDescription());
    preparedStatement.setString(7, contactHistoryRecord.getMsgSubject());
    preparedStatement.setString(8, contactHistoryRecord.getMsgBody());
    preparedStatement.setString(9, contactHistoryRecord.getSourceGuid());
    preparedStatement.setString(10, contactHistoryRecord.getEmailFull());
    preparedStatement.setNull(11, Types.VARCHAR); // revisit if we need to save phone number
    preparedStatement.setLong(12, contactHistoryRecord.getPrefTypeId() == null
        ? DEFAULT_UNKNOWN_ID : contactHistoryRecord.getPrefTypeId());
    // if we don't care about names just set null for now but we will need to revisit to just drop it once we
    // deprecate the post of contact history from backoffice
    preparedStatement.setNull(13, Types.VARCHAR);

    preparedStatement.setLong(14, contactHistoryRecord.getChannelId() == null
        ? DEFAULT_UNKNOWN_ID : contactHistoryRecord.getChannelId());

    // if we don't care about names just set null for now but we will need to revisit to just drop it once we
    // deprecate the post of contact history from backoffice
    preparedStatement.setNull(15, Types.VARCHAR);


    preparedStatement.setString(16, contactHistoryRecord.getVendorName());
    if (contactHistoryRecord.getOrderId() != null) {
      preparedStatement.setLong(17, contactHistoryRecord.getOrderId());
    } else {
      preparedStatement.setNull(17, Types.BIGINT);
    }
    preparedStatement.setString(18, contactHistoryRecord.getDeviceId());
    preparedStatement.setDate(19, contactHistoryRecord.getSentDate() == null
        ? new Date(Instant.now().toEpochMilli()) : new Date(contactHistoryRecord.getSentDate()));
    preparedStatement.setNull(20, Types.CHAR);
    preparedStatement.setString(21, contactHistoryRecord.getMsgIdentifier());
    if (contactHistoryRecord.getSubscriptionId() != null) {
      preparedStatement.setLong(22, contactHistoryRecord.getSubscriptionId());
    } else {
      preparedStatement.setNull(22, Types.BIGINT);
    }
    if (contactHistoryRecord.getMemberId() != null) {
      preparedStatement.setLong(23, contactHistoryRecord.getMemberId());
    } else {
      preparedStatement.setNull(23, Types.BIGINT);
    }
    preparedStatement.setString(24, contactHistoryRecord.getFaxNumber());
    preparedStatement.setString(25, contactHistoryRecord.getReturnId());
    preparedStatement.setString(26, contactHistoryRecord.getOutOfStockPartNumbers());

    preparedStatement.executeUpdate();

    ResultSet rs = preparedStatement.getGeneratedKeys();

    return rs.next() ? rs.getLong(1) : null;
  }

  /*
  Currently the get contact history functionality is exposed in Oracle Service Cloud only.  Whenever any changes are
  made be sure to confirm that existing functionality is not impacted.
   */
  public ContactHistoryWrapper getContactHistory_V1(ContactHistorySearchCriteria criteria) throws SQLException {
    StringBuilder sqBuilder = new StringBuilder();

    //number of days starts with today
    LocalDate toDate = LocalDate.now().plusDays(1);
    LocalDate fromDate = LocalDate.now().minusDays(criteria.getDays() - 1);

    sqBuilder.append("SELECT ch.contact_history_id, ch.source_system_id, pt.pref_type_nm, pat.pref_act_type_nm, ch.order_id, ch.email_full, ");
    sqBuilder.append("ch.message_subject, ch.message_body, ch.vendor_nm, ch.create_dt ");
    sqBuilder.append("FROM notfctr.contact_history ch ");
    sqBuilder.append("INNER JOIN prefctr.pref_action_type pat ON ch.pref_act_type_id = pat.pref_act_type_id ");
    sqBuilder.append("INNER JOIN prefctr.pref_type pt ON ch.pref_type_id = pt.pref_type_id ");
    sqBuilder.append("WHERE source_entity = 'CUSTOMERNO' AND source_entity_value = '").append(criteria.getCustomerId()).append("' ");
    sqBuilder.append("AND ch.create_dt >= '" + fromDate.toString()).append("' ");
    sqBuilder.append("AND ch.create_dt < '" + toDate.toString()).append("' ");

    if (criteria.getOrderId() != null) {
      sqBuilder.append("AND ch.order_id = ").append(criteria.getOrderId()).append(" ");
    }
    if (criteria.getChannels() != null && !criteria.getChannels().isEmpty()) {
      sqBuilder.append("AND ch.pref_act_type_id in (SELECT pat.pref_act_type_id FROM prefctr.pref_action_type WHERE pat.pref_act_type_nm in ").append(
          QueryUtils.formatInOperator(criteria.getChannels())).append(") ");
    }
    if (criteria.getVendors() != null && !criteria.getVendors().isEmpty()) {
      sqBuilder.append("AND ch.vendor_nm in ").append(
          QueryUtils.formatInOperator(criteria.getVendors())).append(" ");
    }
    sqBuilder.append("ORDER BY ch.create_dt DESC");

    try (Connection conn = DriverManager.getConnection(DatabaseHelper.getURL());
         Statement stmt = conn.createStatement()) {

      ResultSet rs = stmt.executeQuery(sqBuilder.toString());

      List<ContactHistoryRecordV1> contactHistoryV1List = new ArrayList<>();
      while (rs.next()) {
        //Retrieve by column name
        ContactHistoryRecordV1 chr = new ContactHistoryRecordV1();
        chr.setContactHistoryId(rs.getLong("contact_history_id"));
        chr.setSourceSystemId(rs.getInt("source_system_id"));
        chr.setCategory(rs.getString("pref_type_nm"));
        chr.setChannel(rs.getString("pref_act_type_nm"));

        Long orderId = rs.getLong("order_id");
        if (!rs.wasNull()) {
          chr.setOrderId(orderId);
        }

        chr.setEmailSubject(rs.getString("message_subject"));
        chr.setPlainTextBody(rs.getString("message_body"));
        chr.setEmailRecipient(rs.getString("email_full"));
        chr.setVendor(rs.getString("vendor_nm"));
        chr.setSentDate(rs.getTimestamp("create_dt"));

        contactHistoryV1List.add(chr);
      }
      return new ContactHistoryWrapper(contactHistoryV1List);
    }
  }

  public boolean checkExistingContactHistory(String idKey, String idValue, Long templateId, String rid) throws SQLException {
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append(CHECK_EXISTING_CONTACT_HISTORY);
    sqlBuilder.append("AND ch.create_dt BETWEEN NOW() - INTERVAL '");
    sqlBuilder.append(System.getenv(ServiceConstants.CONTACT_HISTORY_LOOKBACK_WINDOW_ENV_VAR));
    sqlBuilder.append("MINUTES' AND NOW();");

    try (Connection conn = DriverManager.getConnection(DatabaseHelper.getURL());
         PreparedStatement preparedStatement = conn.prepareStatement(sqlBuilder.toString())) {
      preparedStatement.setString(1, idKey);
      preparedStatement.setString(2, idValue);
      preparedStatement.setLong(3,templateId);
      preparedStatement.setString(4,rid);

       ResultSet rs = preparedStatement.executeQuery();
       return rs.next();
    }
  }

  public boolean checkIfEverSent(String idKey, String idValue, Long templateId) throws SQLException {
    try (Connection conn = DriverManager.getConnection(DatabaseHelper.getURL());
         PreparedStatement preparedStatement = conn.prepareStatement(CHECK_EVER_SENT)) {
      preparedStatement.setString(1, idKey);
      preparedStatement.setString(2, idValue);
      preparedStatement.setLong(3,templateId);

      ResultSet rs = preparedStatement.executeQuery();
      return rs.next();
    }
  }

  @Data
  public static class ContactHistorySearchCriteria {
    private final String customerId;
    private final int days;

    private Long orderId;
    private List<String> channels;
    private List<String> vendors;

    public ContactHistorySearchCriteria(String customerId, int days) {
      this.customerId = customerId;
      this.days = days;
    }

  }

  /*
  Required to support batch post contact history via back office
   */
  public List<ContactHistoryResponse> insertContactHistory(List<ContactHistoryRequest> contactHistoryRequests)
      throws SQLException, ClassNotFoundException, IllegalArgumentException {

    try (Connection conn = DriverManager.getConnection(DatabaseHelper.getURL());
         PreparedStatement preparedStatement =
             conn.prepareStatement(INSERT_CONTACT_HISTORY_QUERY_FOR_BATCH, new String[]{"contact_history_id"})) {

      List<ContactHistoryResponse> contactHistoryResponses = new ArrayList<>();
      for (ContactHistoryRequest contactHistoryRequest : contactHistoryRequests) {
        for (ChannelMetadata channelMap : contactHistoryRequest.getChannelMetadata()) {
          try {
            ContactHistoryResponse response = insertContactHistory(conn, preparedStatement, contactHistoryRequest, channelMap);
            contactHistoryResponses.add(response);
          } catch (Exception ex) {
            log.error("failure writing to contact history for request {} due to {}", channelMap.toString(), ex.toString());
          }
        }
      }

      return contactHistoryResponses;
    }
  }

  private ContactHistoryResponse insertContactHistory(Connection conn, PreparedStatement preparedStatement,
                                                      ContactHistoryRequest contactHistoryRequest, ChannelMetadata channelMap)
      throws SQLException, ClassNotFoundException, IllegalArgumentException {

////    preparedStatement.setInt(1, Integer.valueOf(contactHistoryRequest.getSourceSystemId()));
    preparedStatement.setInt(1, -1); // source system isn't seeded yet so violating constraint... this is temporary
//    preparedStatement.setInt(1, contactHistoryRequest.getSourceSystemId() == null
//        ? -1 : contactHistoryRequest.getSourceSystemId());

    if (channelMap.getSourceKeyId() == null) {
      preparedStatement.setNull(2, Types.BIGINT);
    } else {
      preparedStatement.setLong(2, channelMap.getSourceKeyId());
    }
    // TODO think about being more defensive here to avoid npes if pref details are ever not required
    preparedStatement.setString(3, contactHistoryRequest.getPreferenceDetails().getIdKey());
    preparedStatement.setString(4, contactHistoryRequest.getPreferenceDetails().getIdValue());
    preparedStatement.setLong(5, channelMap.getMsgTemplateId() == null
        ? DEFAULT_UNKNOWN_ID : channelMap.getMsgTemplateId());
    preparedStatement.setNull(6, Types.VARCHAR);
    preparedStatement.setString(7, channelMap.getSubject());
    preparedStatement.setString(8, channelMap.getPlainTextBody());
    preparedStatement.setString(9, channelMap.getSourceGuid());
    preparedStatement.setString(10, channelMap.getRecipient());
    preparedStatement.setNull(11, Types.VARCHAR);
    preparedStatement.setLong(12, contactHistoryRequest.getPrefTypeId() == null
        ? DEFAULT_UNKNOWN_ID : contactHistoryRequest.getPrefTypeId());
    preparedStatement.setString(13, contactHistoryRequest.getPrefTypeName());
    preparedStatement.setString(14, Strings.isBlank(channelMap.getChannel())
        ? DEFAULT_UNKNOWN : channelMap.getChannel().toUpperCase());
    preparedStatement.setString(15, Strings.isBlank(channelMap.getChannel())
        ? DEFAULT_UNKNOWN : channelMap.getChannel().toUpperCase());
    preparedStatement.setString(16, Strings.isBlank(channelMap.getVendorName())
        ? channelMap.getVendorName() : channelMap.getVendorName().toUpperCase());
    if (Strings.isBlank(channelMap.getOrderId())) {
      preparedStatement.setNull(17, Types.BIGINT);
    } else {
      preparedStatement.setLong(17, Integer.valueOf(channelMap.getOrderId()));
    }
    preparedStatement.setString(18, channelMap.getDeviceId());
    preparedStatement.setDate(19, channelMap.getSentDate() == null
        ? new Date(Instant.now().toEpochMilli()) : new Date(channelMap.getSentDate()));
    preparedStatement.setNull(20, Types.CHAR);
    preparedStatement.setString(21, channelMap.getMsgIdentifier());

    preparedStatement.executeUpdate();

    ResultSet rs = preparedStatement.getGeneratedKeys();

    if (rs.next()) {
      return new ContactHistoryResponse(rs.getLong(1));
    }
    return new ContactHistoryResponse();
  }

}