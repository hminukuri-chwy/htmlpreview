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
public class Pet {
  @JsonProperty("Name")
  private String name;
  @JsonProperty("Age")
  private String age;
  @JsonProperty("Sex")
  private String sex;
  @JsonProperty("Weight")
  private int weight;
  @JsonProperty("Species")
  private String species;
  @JsonProperty("Breed")
  private String breed;
  @JsonProperty("SpayedNeutered")
  private String SpayedNeutered;
  @JsonProperty("Allergies")
  private String allergies;
  @JsonProperty("Medications")
  private String medications;
  @JsonProperty("Conditions")
  private String conditions;
}