package com.chewy.domain.notfctr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class MessageTemplate {

  // id of the message template
  @JsonProperty("id")
  private Long id;

  // name of the message template
  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("version")
  private String version;

  @JsonProperty("effDate")
  private Long effDate;

  @JsonProperty("expDate")
  private Long expDate;

  // campaign id associated with any notification sent using this template
  @JsonProperty("campaignId")
  private String campaignId;

  @JsonProperty("campaignName")
  private String campaignName;

  // the id of the channel that any notification sent using this template should use
  @JsonProperty("channelId")
  private Long channelId;   // by using id we are forced to join.... should have used name?

  private List<MessageTemplateDetail> templateDetails;

  @JsonProperty("sender")
  private String sender;

  @JsonProperty("label")
  private String label;

}