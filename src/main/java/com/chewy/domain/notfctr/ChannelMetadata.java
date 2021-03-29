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
public class ChannelMetadata {
  private Integer sourceKeyId;
  private String channel;
  private Long channelId;
  private String subject;
  private String recipient;
  private String vendorName;
  private String orderId;
  private String plainTextBody;
  private String sourceGuid;
  private String msgIdentifier;
  private String phoneNumber;
  private String deviceId;
  private Long sentDate;
  private Long msgTemplateId;
}
