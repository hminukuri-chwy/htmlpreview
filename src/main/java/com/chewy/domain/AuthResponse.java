package com.chewy.domain;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Introspected
public class AuthResponse {
  private String access_token;
  private String expires_in;
}
