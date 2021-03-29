package com.chewy.requests;

import com.chewy.domain.notfctr.ChannelMetadata;
import com.chewy.domain.prefctr.PreferenceDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Introspected
public class ContactHistoryRequest {
  @JsonProperty("sourceSystemId")
  private Integer sourceSystemId;

  @JsonProperty("category")
  private String category;

  @JsonProperty("prefTypeId")
  private Long prefTypeId;

  @JsonProperty("prefTypeName")
  private String prefTypeName;

  // keep for now so we can move this to the channel map during
  // transformation but we shouldn't need it long term
  @JsonProperty("sourceGuid")
  private String sourceGuid;

  @JsonProperty("preferenceDetails")
  private PreferenceDetails preferenceDetails;

  @JsonProperty("channelMetadata")
  @Builder.Default
  private List<ChannelMetadata> channelMetadata = null;
}
