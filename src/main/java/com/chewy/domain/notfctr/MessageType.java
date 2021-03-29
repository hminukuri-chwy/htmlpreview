package com.chewy.domain.notfctr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class MessageType {
  // name of the message type as defined by NPC
  private String messageTypeName;

  // name of the message type as defined by the event source
  private String sourceMessageTypeName;

  // id of the message type
  private Long messageTypeId;

  // id of the preference that drives this notification
  private Long prefTypeId;

  // subject to attach to any notification (like email) sent for this message type
  // should move this to the template details.....allows us to make it dynamic with ftl
  private String subject;

  private String description;

}