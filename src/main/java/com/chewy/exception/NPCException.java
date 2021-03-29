package com.chewy.exception;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class NPCException extends Exception {
  private NPCErrorCode errorCode = NPCErrorCode.UNKNOWN;
  private String description;

  public NPCException() {
    super();
  }

  public NPCException(NPCErrorCode errorCode, String description, Throwable throwable) {
    super(throwable);
    this.errorCode = errorCode;
    this.description = description;
  }

  public NPCException(NPCErrorCode errorCode, String description) {
    super(description);
    this.errorCode = errorCode;
    this.description = description;
  }

  public NPCException(NPCErrorCode errorCode, Throwable throwable) {
    super(throwable);
    this.errorCode = errorCode;
  }

  public NPCException(NPCErrorCode errorCode) {
    super();
    this.errorCode = errorCode;
  }

  public NPCException(String s) {
    super(s);
    this.description = s;
  }

  public NPCException(String s, Throwable throwable) {
    super(s, throwable);
    this.description = s;
  }

  public NPCException(Throwable throwable) {
    super(throwable);
  }

  public NPCErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public String getMessage() {
    if (description == null) {
      return errorCode.toString();
    } else {
      return errorCode + ": " + description;
    }
  }

  public String getDescription() {
    return description;
  }

}
