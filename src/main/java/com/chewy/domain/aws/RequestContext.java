package com.chewy.domain.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RequestContext {
  @JsonProperty("accountId")
  public String accountId;
  @JsonProperty("resourceId")
  public String resourceId;
  @JsonProperty("stage")
  public String stage;
  @JsonProperty("requestId")
  public String requestId;
  @JsonProperty("requestTime")
  public String requestTime;
  @JsonProperty("requestTimeEpoch")
  public Long requestTimeEpoch;
  @JsonProperty("identity")
  public Identity identity;
  @JsonProperty("path")
  public String path;
  @JsonProperty("resourcePath")
  public String resourcePath;
  @JsonProperty("httpMethod")
  public String httpMethod;
  @JsonProperty("apiId")
  public String apiId;
  @JsonProperty("protocol")
  public String protocol;
}
