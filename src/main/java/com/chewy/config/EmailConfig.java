package com.chewy.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.inject.Singleton;

@ConfigurationProperties("email")
@Data
@Singleton
@Introspected
public class EmailConfig {
  private String host;
  private String auth;
  private String protocol;
  private String smtpTimeout;
  private String connectTimeout;

}