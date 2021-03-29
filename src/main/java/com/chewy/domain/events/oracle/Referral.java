package com.chewy.domain.events.oracle;//package example.domain.events.oracle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Introspected
public class Referral {
  @JsonProperty("Name")
  private String name;
  @JsonProperty("Phone")
  private String phone;
  @JsonProperty("Address")
  private String address;
  @JsonProperty("Link")
  private String link;
}