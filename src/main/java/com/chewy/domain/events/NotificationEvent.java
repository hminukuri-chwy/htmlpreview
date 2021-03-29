package com.chewy.domain.events;//package example.domain.events;

import com.chewy.Channel;
import com.chewy.domain.prefctr.PreferenceDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class NotificationEvent {

  private Long firstProcessedTimeStamp;
  private int numTimesProcessed;
  private Set<Channel> processedChannels;
  private Set<Channel> requiredChannels;
  /*
  The business use case / event type the notification represents
  i.e. out of stock or ship confirmation
  */
  private String type;
  /*
  id of the system from which the event is being generated
   */
  private int sourceSystemId;
  /*
  correlation id to trace events through the system
   */
  private String rid;
  /*
  Data required to retrieve preferences for the given event type
   */
  private PreferenceDetails preferenceDetails;
  /*
  primary fax number
   */
  private String faxNumber;

  /*
  List of return labels
   */
  private List<String> returnLabels;
  /*
  primary email address
   */
  private String recipient;
  /*
  cc email address
   */
  private String recipientCC;
  /*
  bcc email address
   */
  private String recipientBCC;
  private Boolean isComposed;
  private Long subscriptionId;
  private Long memberId;
  private String returnId;
  private String outOfStockPartNumbers;
  /*
  The meta data required to support sending notifications for
  the given type.  This is the data that will be used to fill out
  any messaging templates internally or via a 3rd party integration
   */
  @Builder.Default
  private Map<String,Object> notificationMetaData = new HashMap<>();

  public HashMap<String,Object> getTriggerProperties() {
    HashMap<String, Object> triggerProperties = new HashMap<>();
    for (Map.Entry<String, Object> metadata : notificationMetaData.entrySet()) {
      if (metadata.getValue() instanceof String || metadata.getValue() instanceof Long) {
        triggerProperties.put(metadata.getKey(), metadata.getValue());
      }
    }
    return triggerProperties;
  }
}