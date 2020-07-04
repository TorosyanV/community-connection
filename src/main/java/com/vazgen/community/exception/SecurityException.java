package com.vazgen.community.exception;

public class SecurityException extends CommunityException {

  public SecurityException(String message) {
    super(message);
  }

  public SecurityException(Throwable cause) {
    super(cause);
  }
}
