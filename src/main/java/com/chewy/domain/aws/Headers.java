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
public class Headers {
  @JsonProperty("Accept")
  public String accept;
  @JsonProperty("Accept-Encoding")
  public String acceptEncoding;
  @JsonProperty("Accept-Language")
  public String acceptLanguage;
  @JsonProperty("Cache-Control")
  public String cacheControl;
  @JsonProperty("CloudFront-Forwarded-Proto")
  public String cloudFrontForwardedProto;
  @JsonProperty("CloudFront-Is-Desktop-Viewer")
  public String cloudFrontIsDesktopViewer;
  @JsonProperty("CloudFront-Is-Mobile-Viewer")
  public String cloudFrontIsMobileViewer;
  @JsonProperty("CloudFront-Is-SmartTV-Viewer")
  public String cloudFrontIsSmartTVViewer;
  @JsonProperty("CloudFront-Is-Tablet-Viewer")
  public String cloudFrontIsTabletViewer;
  @JsonProperty("CloudFront-Viewer-Country")
  public String cloudFrontViewerCountry;
  @JsonProperty("Host")
  public String host;
  @JsonProperty("Upgrade-Insecure-Requests")
  public String upgradeInsecureRequests;
  @JsonProperty("User-Agent")
  public String userAgent;
  @JsonProperty("Via")
  public String via;
  @JsonProperty("X-Amz-Cf-Id")
  public String xAmzCfId;
  @JsonProperty("X-Forwarded-For")
  public String xForwardedFor;
  @JsonProperty("X-Forwarded-Port")
  public String xForwardedPort;
  @JsonProperty("X-Forwarded-Proto")
  public String xForwardedProto;
  @JsonProperty("X-Correlation-Id")
  public String xCorrelationId;  // will this work?
}
