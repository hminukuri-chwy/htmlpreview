package com.chewy.config;


import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import javax.inject.Singleton;


@ConfigurationProperties("auth")
@Data
@Singleton
@Introspected
public class AuthConfig {
    private String url;
}
