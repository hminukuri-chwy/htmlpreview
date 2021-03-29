package com.chewy.authenticate;
import io.micronaut.core.annotation.Introspected;

import javax.inject.Singleton;
import java.time.Instant;
import java.util.Date;

@Singleton
@Introspected
public class TokenCache {

  private String token;
  private Date expiresAt;


  public TokenCache() {
    this(null, null);
  }

  public TokenCache(String token, Date expiresAt) {
    this.token = token;
    this.expiresAt = expiresAt;
  }

  public String get() {
    if (expiresAt != null && Date.from(Instant.now()).after(expiresAt)) {
      // Token in the Cache is Expired
      clear();
      return null;
    } else {
      // Cache is Empty or the cached Token is not expired
      return token;
    }
  }

  public void put(String token, Date expiresAt) {
    if (token == null || expiresAt == null) {
      clear();
    } else {
      this.token = token;
      this.expiresAt = expiresAt;
    }
  }

  public void clear() {
    token = null;
    expiresAt = null;
  }

  public boolean isCleared() {
    return token == null;
  }

  public boolean isExpired() {
    if (expiresAt != null && Date.from(Instant.now()).after(expiresAt)) {
      clear();
      return true;
    } else {
      return false;
    }
  }

}
