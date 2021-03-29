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
public class Identity {
  @JsonProperty("cognitoIdentityPoolId")
  public Object cognitoIdentityPoolId;
  @JsonProperty("accountId")
  public Object accountId;
  @JsonProperty("cognitoIdentityId")
  public Object cognitoIdentityId;
  @JsonProperty("caller")
  public Object caller;
  @JsonProperty("accessKey")
  public Object accessKey;
  @JsonProperty("sourceIp")
  public String sourceIp;
  @JsonProperty("cognitoAuthenticationType")
  public Object cognitoAuthenticationType;
  @JsonProperty("cognitoAuthenticationProvider")
  public Object cognitoAuthenticationProvider;
  @JsonProperty("userArn")
  public Object userArn;
  @JsonProperty("userAgent")
  public String userAgent;
  @JsonProperty("user")
  public Object user;
}
