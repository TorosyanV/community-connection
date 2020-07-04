package com.vazgen.community.exception;

public class UserAlreadyExistsException extends SecurityException {

  public UserAlreadyExistsException(String username) {
    super("user with " + username + "already exists");
  }

  public UserAlreadyExistsException(Throwable cause) {
    super(cause);
  }
}
