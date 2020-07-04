package com.vazgen.community.exception;

public class InvalidOwnerExceptionException extends SecurityException {

  public InvalidOwnerExceptionException(Long userId, Long entityId) {
    super(String.format("user %s is not owner of entity %s", userId, entityId));
  }
}
