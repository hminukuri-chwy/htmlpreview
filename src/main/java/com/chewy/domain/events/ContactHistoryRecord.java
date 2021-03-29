package com.chewy.domain.events;//package example.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class ContactHistoryRecord {

  // how can we avoid doing this maybe we need a contact history event
  // that has the rid, num times processed and then a contact history record
  private String rid;

  private Integer sourceSystemId;
  private Long sourceKeyId;
  private String sourceEntity;
  private String sourceEntityValue;
  private Long msgTemplateId;
  private String msgDescription;
  private String msgSubject;
  private String msgBody;
  // uuid of the message - unique per message
  private String msgIdentifier;
  // uuid of the request that triggered the notification (rid) allows us to tie
  // all notification sent as a result of one inbound event together
  private String sourceGuid;
  private String emailFull;
  private String phoneFull;
  private String faxNumber;
  private String prefTypeName;
  private Long prefTypeId;
  private String channel;
  private Long channelId;
  private String vendorName;
  private Long orderId;
  private Long subscriptionId;
  private Long memberId;
  private String returnId;
  private String deviceId;
  private String outOfStockPartNumbers;
  private Long sentDate;

  private Date timeEmittedToContactHistory;

}