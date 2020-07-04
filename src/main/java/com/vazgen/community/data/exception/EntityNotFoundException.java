package com.vazgen.community.data.exception;

public abstract class EntityNotFoundException extends RuntimeException {

  private final long id;

  public <T extends Class<?>> EntityNotFoundException(T type, long id) {

    super(String.format("%s not found: id = %s", type.getSimpleName(), id));
    this.id = id;
  }

  public <T extends Class<?>> EntityNotFoundException(T type, String message) {
    super(String.format("%s not found, %s", type.getSimpleName(), message));
    id = 0;
  }

  public long getId() {
    return id;
  }
}
