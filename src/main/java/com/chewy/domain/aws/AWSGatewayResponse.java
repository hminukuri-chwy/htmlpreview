package com.chewy.domain.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class AWSGatewayResponse {
  @JsonProperty("isBase64Encoded")
  public Boolean isBase64Encoded;
  @JsonProperty("statusCode")
  public Integer statusCode;
  @JsonProperty("headers")
  @Builder.Default
  public Map<String,String> headers = null;
  @JsonProperty("multiValueHeaders")
  public MultiValueHeaders multiValueHeaders;
  @JsonProperty("body")
  public String body;
}
