package com.chewy.domain.notfctr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Introspected
public class ContactHistoryRecordV1 {
  @JsonProperty("contact_history_id")
  private long contactHistoryId;

  @JsonProperty("source_system_id")
  private int sourceSystemId;

  private String category;

  private String channel;

  @JsonProperty("email_recipient")
  private String emailRecipient;

  @JsonProperty("email_subject")
  private String emailSubject;

  @JsonProperty("email_body")
  private String plainTextBody;

  private String vendor;

  @JsonProperty("order_id")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long orderId;

  @JsonProperty("sent_date")
  @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="America/New_York")
  private Date sentDate;
}