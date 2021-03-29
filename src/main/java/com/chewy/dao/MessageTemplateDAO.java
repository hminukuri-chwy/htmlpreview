package com.chewy.dao;

import com.chewy.domain.notfctr.MessageTemplate;
import com.chewy.domain.notfctr.MessageTemplateDetail;
import com.chewy.domain.notfctr.MessageType;
import com.chewy.domain.notfctr.MessageTypeConfiguration;
import com.chewy.util.Strings;
import com.google.common.collect.ImmutableList;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;
import java.sql.*;
import java.util.*;

import static com.chewy.helper.DatabaseHelper.getURL;

@Singleton
@Introspected
public class MessageTemplateDAO {

  private static final String RETRIEVE_ALL_PREF_TYPE_IDS =
      "SELECT mt.message_type_nm, mt.pref_type_id FROM notfctr.message_type mt";

  public Long getPrefTypeIdByMessageType(String messageType) throws SQLException {
    StringBuilder sqBuilder = new StringBuilder();
    sqBuilder.append("SELECT mt.pref_type_id ");
    sqBuilder.append("FROM notfctr.message_type mt ");
    sqBuilder.append("WHERE mt.message_type_nm = '").append(messageType).append("' OR mt.source_key_cd = '").append(messageType).append("'");

    try (Connection conn = DriverManager.getConnection(getURL());
         Statement stmt = conn.createStatement()) {

      ResultSet rs = stmt.executeQuery(sqBuilder.toString());

      Long prefTypeId = null;
      while (rs.next()) {
        prefTypeId = rs.getLong("pref_type_id");

      }
      return prefTypeId;
    }
  }

  public MessageTypeConfiguration getMessageTypeConfiguration(MessageTemplateSearchCriteria criteria) throws SQLException {
    StringBuilder sqBuilder = new StringBuilder();

    sqBuilder.append("SELECT " +
        "mt.message_type_id, " +
        "mt.message_type_nm, " +
        "mt.message_type_desc, " +
        "mt.source_system_id, " +
        "mt.source_key_cd, " +
        "mt.pref_type_id, " +
        "mt.subject_desc, " +
        "mtmp.message_template_id, " +
        "mtmp.template_nm, " +
        "mtmp.template_desc, " +
        "mtmp.template_ver, " +
        "mtmp.template_eff_dt, "  +
        "mtmp.template_exp_dt, " +
        "mtmp.pref_act_type_id, " +
        "mtmp.campaign_id, " +
        "mtmp.campaign_nm, " +
        "mtmp.source_system_id, " +
        "mtmp.message_sender, " +
        "mtmp.message_sender_label," +
        "mtmpd.message_template_detail_id, " +
        "mtmpd.template_type_cd, " +
        "mtmpd.template_file_nm, " +
        "mtmpd.attachment_file_nm, " +
        "mtmpd.template_detail_ver, " +
        "mtmpd.template_detail_eff_dt, " +
        "mtmpd.template_detail_exp_dt ");
    sqBuilder.append("FROM notfctr.message_type mt ");
    sqBuilder.append("INNER JOIN notfctr.message_template mtmp ON mt.message_type_id = mtmp.message_type_id ");
    sqBuilder.append("INNER JOIN notfctr.message_template_detail mtmpd ON mtmp.message_template_id = mtmpd.message_template_id ");
    sqBuilder.append("WHERE (mt.message_type_nm = '").append(criteria.getType()).append("' OR mt.source_key_cd = '").append(criteria.getType()).append("') ");

    if (Strings.isNotBlank(criteria.getChannel())) {
      sqBuilder.append("AND mtmp.pref_act_type_id = (SELECT pat.pref_act_type_id FROM prefctr.pref_action_type pat WHERE pref_act_type_nm = '").append(criteria.getChannel().toUpperCase()).append("') ");
    }
    if (criteria.getActive() != null && criteria.getActive()) {
      sqBuilder.append("AND CURRENT_TIMESTAMP BETWEEN mtmp.template_eff_dt AND mtmp.template_exp_dt ");
      sqBuilder.append("AND CURRENT_TIMESTAMP BETWEEN mtmpd.template_detail_eff_dt AND mtmpd.template_detail_exp_dt");
    }

    try (Connection conn = DriverManager.getConnection(getURL()); Statement stmt = conn.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqBuilder.toString());
      MessageType messageType = null;
      Map<Long, MessageTemplate> templateMap = new HashMap<>();
      while (rs.next()) {
        if (messageType == null) {
          MessageType.MessageTypeBuilder mTypeBuilder = MessageType.builder();
          mTypeBuilder.messageTypeId(rs.getLong("message_type_id"));
          mTypeBuilder.messageTypeName(rs.getString("message_type_nm"));
          mTypeBuilder.sourceMessageTypeName(rs.getString("source_key_cd"));
          mTypeBuilder.prefTypeId(rs.getLong("pref_type_id"));
          mTypeBuilder.subject(rs.getString("subject_desc")); // this should move to db config
          mTypeBuilder.description(rs.getString("message_type_desc"));
          messageType = mTypeBuilder.build();
        }

        Long messageTemplateID = rs.getLong("message_template_id");
        if (templateMap.containsKey(messageTemplateID)) {
          MessageTemplate currentTemplate = templateMap.get(messageTemplateID);
          List<MessageTemplateDetail> currentDetails = currentTemplate.getTemplateDetails();
          MessageTemplateDetail.MessageTemplateDetailBuilder mTempDetailBuilder = MessageTemplateDetail.builder();
          mTempDetailBuilder.type(rs.getString("template_type_cd"));
          mTempDetailBuilder.templateFileName(rs.getString("template_file_nm"));
          mTempDetailBuilder.outboundAttachmentName((rs.getString("attachment_file_nm")));
          mTempDetailBuilder.version(rs.getString("template_detail_ver"));
//          mTempDetailBuilder.effDate(rs.getDate("template_detail_eff_dt").toInstant().toEpochMilli());
//          mTempDetailBuilder.expDate(rs.getDate("template_detail_exp_dt").toInstant().toEpochMilli());
          MessageTemplateDetail messageTemplateDetail = mTempDetailBuilder.build();
          currentDetails.add(messageTemplateDetail);
          currentTemplate.setTemplateDetails(currentDetails);
          templateMap.put(messageTemplateID, currentTemplate);
        } else {
          MessageTemplate.MessageTemplateBuilder mTemplateBuilder = MessageTemplate.builder();
          mTemplateBuilder.id(rs.getLong("message_template_id"));
          mTemplateBuilder.name(rs.getString("template_nm"));
          mTemplateBuilder.description(rs.getString("template_desc"));
          mTemplateBuilder.version(rs.getString("template_ver"));
//          mTemplateBuilder.effDate(rs.getDate("template_eff_dt").toInstant().toEpochMilli());
//          mTemplateBuilder.expDate(rs.getDate("template_exp_dt").toInstant().toEpochMilli());
          mTemplateBuilder.campaignId(rs.getString("campaign_id"));
          mTemplateBuilder.campaignName(rs.getString("campaign_nm"));
          mTemplateBuilder.channelId(rs.getLong("pref_act_type_id"));
          mTemplateBuilder.sender(rs.getString("message_sender"));
          mTemplateBuilder.label(rs.getString("message_sender_label"));


          MessageTemplateDetail.MessageTemplateDetailBuilder mTempDetailBuilder = MessageTemplateDetail.builder();
          mTempDetailBuilder.type(rs.getString("template_type_cd"));
          mTempDetailBuilder.templateFileName(rs.getString("template_file_nm"));
          mTempDetailBuilder.version(rs.getString("template_detail_ver"));
          mTempDetailBuilder.outboundAttachmentName((rs.getString("attachment_file_nm")));
//          mTempDetailBuilder.effDate(rs.getDate("template_detail_eff_dt").);
//          mTempDetailBuilder.expDate(rs.getLong("template_detail_exp_dt"));
          MessageTemplateDetail messageTemplateDetail = mTempDetailBuilder.build();
          List<MessageTemplateDetail> templateDetails = new ArrayList<>();
          templateDetails.add(messageTemplateDetail);
          mTemplateBuilder.templateDetails(templateDetails);
          templateMap.put(messageTemplateID, mTemplateBuilder.build());
        }
      }

      List<MessageTemplate> messageTemplates =
          templateMap.values().stream().collect(ImmutableList.toImmutableList());

      return new MessageTypeConfiguration(messageType, messageTemplates);

    }
  }

  @Data
  public static class MessageTemplateSearchCriteria {
    @Getter
    private String type;
    @Getter @Setter
    private Boolean active;
    @Getter @Setter
    private String channel;

    public MessageTemplateSearchCriteria(String type, Boolean active, String channel) {
      this.type = type;
      this.active = active;
      if (Strings.isNotBlank(channel)) {
        this.channel = channel;
      }
    }

    public MessageTemplateSearchCriteria(Boolean active, String channel) {
      this.active = active;
      if (Strings.isNotBlank(channel)) {
        this.channel = channel;
      }
    }
  }

  public Map<String, Long> readAllPrefTypeIds() throws SQLException {

    Map<String, Long> configMap = new HashMap<>();
    try (Connection conn = DriverManager.getConnection(getURL()); Statement stmt = conn.createStatement()) {
      ResultSet rs = stmt.executeQuery(RETRIEVE_ALL_PREF_TYPE_IDS);

      while (rs.next()) {
        configMap.putIfAbsent(rs.getString("message_type_nm"), rs.getLong("pref_type_id"));
      }
    }
    return configMap;
  }



  ///////////////////////////////////////////////////
  public Map<String, MessageTypeConfiguration> readAllConfigForActiveMessageTypes(MessageTemplateSearchCriteria criteria) throws SQLException {
    StringBuilder sqBuilder = new StringBuilder();

    sqBuilder.append("SELECT " +
            "mt.message_type_id, " +
            "mt.message_type_nm, " +
            "mt.message_type_desc, " +
            "mt.source_system_id, " +
            "mt.source_key_cd, " +
            "mt.pref_type_id, " +
            "mt.subject_desc, " +
            "mtmp.message_template_id, " +
            "mtmp.template_nm, " +
            "mtmp.template_desc, " +
            "mtmp.template_ver, " +
            "mtmp.template_eff_dt, " +
            "mtmp.template_exp_dt, " +
            "mtmp.pref_act_type_id, " +
            "mtmp.campaign_id, " +
            "mtmp.campaign_nm, " +
            "mtmp.source_system_id, " +
            "mtmp.message_sender, " +
            "mtmp.message_sender_label," +
            "mtmpd.message_template_detail_id, " +
            "mtmpd.template_type_cd, " +
            "mtmpd.template_file_nm, " +
            "mtmpd.attachment_file_nm, " +
            "mtmpd.template_detail_ver, " +
            "mtmpd.template_detail_eff_dt, " +
            "mtmpd.template_detail_exp_dt ");
    sqBuilder.append("FROM notfctr.message_type mt ");
    sqBuilder.append("INNER JOIN notfctr.message_template mtmp ON mt.message_type_id = mtmp.message_type_id ");
    sqBuilder.append("INNER JOIN notfctr.message_template_detail mtmpd ON mtmp.message_template_id = mtmpd.message_template_id ");

    if (Strings.isNotBlank(criteria.getChannel())) {
      sqBuilder.append("AND mtmp.pref_act_type_id = (SELECT pat.pref_act_type_id FROM prefctr.pref_action_type pat WHERE pref_act_type_nm = '").append(criteria.getChannel().toUpperCase()).append("') ");
    }
    if (criteria.getActive() != null && criteria.getActive()) {
      sqBuilder.append("AND CURRENT_TIMESTAMP BETWEEN mtmp.template_eff_dt AND mtmp.template_exp_dt ");
      sqBuilder.append("AND CURRENT_TIMESTAMP BETWEEN mtmpd.template_detail_eff_dt AND mtmpd.template_detail_exp_dt");
    }

    try (Connection conn = DriverManager.getConnection(getURL()); Statement stmt = conn.createStatement()) {
      ResultSet rs = stmt.executeQuery(sqBuilder.toString());
      Map<String, MessageTypeConfiguration> configMap = new HashMap<>();

      while (rs.next()) {

        MessageTypeConfiguration messageTypeConfig = null;
        MessageType messageType = null;
        MessageTemplate messageTemplate = null;

        // if we have processed the type before we have a config
        if (configMap.containsKey(rs.getString("message_type_nm"))){
          messageTypeConfig = configMap.get(rs.getString("message_type_nm"));
        } else {
          MessageType.MessageTypeBuilder mTypeBuilder = MessageType.builder();
          mTypeBuilder.messageTypeId(rs.getLong("message_type_id"));
          mTypeBuilder.messageTypeName(rs.getString("message_type_nm"));
          mTypeBuilder.sourceMessageTypeName(rs.getString("source_key_cd"));
          mTypeBuilder.prefTypeId(rs.getLong("pref_type_id"));
          mTypeBuilder.subject(rs.getString("subject_desc")); // this should move to db config
          mTypeBuilder.description(rs.getString("message_type_desc"));
          messageType = mTypeBuilder.build();
        }

        if (messageTypeConfig == null) { // we haven't processed a rs row yet everything is new
          MessageTemplate.MessageTemplateBuilder mTemplateBuilder = MessageTemplate.builder();
          mTemplateBuilder.id(rs.getLong("message_template_id"));
          mTemplateBuilder.name(rs.getString("template_nm"));
          mTemplateBuilder.description(rs.getString("template_desc"));
          mTemplateBuilder.version(rs.getString("template_ver"));
          mTemplateBuilder.campaignId(rs.getString("campaign_id"));
          mTemplateBuilder.campaignName(rs.getString("campaign_nm"));
          mTemplateBuilder.channelId(rs.getLong("pref_act_type_id"));
          mTemplateBuilder.sender(rs.getString("message_sender"));
          mTemplateBuilder.label(rs.getString("message_sender_label"));


          MessageTemplateDetail.MessageTemplateDetailBuilder mTempDetailBuilder = MessageTemplateDetail.builder();
          mTempDetailBuilder.type(rs.getString("template_type_cd"));
          mTempDetailBuilder.templateFileName(rs.getString("template_file_nm"));
          mTempDetailBuilder.version(rs.getString("template_detail_ver"));
          mTempDetailBuilder.outboundAttachmentName((rs.getString("attachment_file_nm")));
          MessageTemplateDetail messageTemplateDetail = mTempDetailBuilder.build();
          List<MessageTemplateDetail> templateDetails = new ArrayList<>();
          templateDetails.add(messageTemplateDetail);
          mTemplateBuilder.templateDetails(templateDetails);
          messageTemplate = mTemplateBuilder.build();

          MessageTypeConfiguration currentConfig =
                  new MessageTypeConfiguration(
                          messageType,
                          new ArrayList<>(Arrays.asList(messageTemplate))
                  );
          configMap.put(messageType.getMessageTypeName(), currentConfig);
        } else {
          // we have at least one message template and one template detail processed
          // since this config mapping is pulled per channel it should only have 1
          // template that fans out to multiple template details
          List<MessageTemplate> templateList = messageTypeConfig.getMessageTemplates();
          MessageTemplate currentTemplate = templateList.get(0);
          List<MessageTemplateDetail> currentDetails = currentTemplate.getTemplateDetails();
          MessageTemplateDetail.MessageTemplateDetailBuilder mTempDetailBuilder = MessageTemplateDetail.builder();
          mTempDetailBuilder.type(rs.getString("template_type_cd"));
          mTempDetailBuilder.templateFileName(rs.getString("template_file_nm"));
          mTempDetailBuilder.outboundAttachmentName((rs.getString("attachment_file_nm")));
          mTempDetailBuilder.version(rs.getString("template_detail_ver"));
          MessageTemplateDetail messageTemplateDetail = mTempDetailBuilder.build();
          currentDetails.add(messageTemplateDetail);
          currentTemplate.setTemplateDetails(currentDetails);
          messageTypeConfig.setMessageTemplates(
                  new ArrayList<>(Arrays.asList(currentTemplate))
          );
        }
      }
      return configMap;
    }
  }
}