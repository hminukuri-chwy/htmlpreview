package com.chewy.domain.events;

import com.chewy.Channel;
import com.chewy.domain.notfctr.MessageTypeConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class OutboundNotificationEvent extends NotificationEvent {
  Channel channel;
  Long orderId;
  String returnId;
  MessageTypeConfiguration messageTypeConfiguration;
}