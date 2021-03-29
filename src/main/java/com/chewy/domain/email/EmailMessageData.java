package com.chewy.domain.email;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class EmailMessageData {
  private String eventType;
  private String msgType;
  private String subject;
  private String plainTextPart;
  private Set<String> attachmentPath;
  private String htmlPart;
  private String recipient;
  private String recipientCC;
  private String recipientBCC;
  private String sender;
  private String label;
  private int sourceSystemId;
  // time notification platform received event - used to track event processing time
  private Long timeReceived;
}
