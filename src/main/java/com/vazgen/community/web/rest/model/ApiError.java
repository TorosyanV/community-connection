package com.vazgen.community.web.rest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ApiError {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private String message;
  private String debugMessage;


  public ApiError(Throwable ex) {
    this("Unexpected error", ex);
  }

  public ApiError(String message, Throwable ex) {
    timestamp = LocalDateTime.now();
    this.message = message;
    this.debugMessage = ex.getLocalizedMessage();
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }


  public String getMessage() {
    return message;
  }


  public String getDebugMessage() {
    return debugMessage;
  }

}
