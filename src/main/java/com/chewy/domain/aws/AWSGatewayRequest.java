package com.chewy.domain.aws;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Introspected
public class AWSGatewayRequest {
  @JsonProperty("body")
  public String body;
  @JsonProperty("resource")
  public String resource;
  @JsonProperty("path")
  public String path;
  @JsonProperty("httpMethod")
  public String httpMethod;
  @JsonProperty("isBase64Encoded")
  public Boolean isBase64Encoded;
  @JsonProperty("queryStringParameters")
  @Builder.Default
  public Map<String,String> queryStringParameters = null;
  @JsonProperty("pathParameters")
  @Builder.Default
  public Map<String,Object>  pathParameters = null;
  @JsonProperty("stageVariables")
  @Builder.Default
  public Map<String,Object> stageVariables  = null;
  @JsonProperty("headers")
  public Headers headers;
  @JsonProperty("requestContext")
  public RequestContext requestContext;
  @JsonProperty("source")
  public String source;
}
