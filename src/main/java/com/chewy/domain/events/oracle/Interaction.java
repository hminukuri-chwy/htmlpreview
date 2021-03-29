package com.chewy.domain.events.oracle;//package example.domain.events.oracle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class Interaction {
  @JsonProperty("Products")
  private LinkContainer products;
  @JsonProperty("Learn")
  private LinkContainer learn;
  @JsonProperty("ReferenceNumber")
  private String referenceNumber;
  @JsonProperty("Time")
  private Date time;
  @JsonProperty("Provider")
  private String provider;
  @JsonProperty("Reason")
  private String reason;
  @JsonProperty("Status")
  private String status;
  @JsonProperty("Summary")
  private String summary;
  @JsonProperty("Recommendation")
  private String recommendation;
  @JsonProperty("Information")
  private String information;
  @JsonProperty("Referral")
  private Referral referral;
}