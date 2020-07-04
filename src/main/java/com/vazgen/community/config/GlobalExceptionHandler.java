package com.vazgen.community.config;

import com.vazgen.community.data.exception.EntityNotFoundException;
import com.vazgen.community.web.rest.model.ApiError;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> defaultErrorHandler(HttpServletRequest req, Exception e) {
    logError(req, e);
    return getErrorMessage(e, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> httpMessageNotReadableExceptionHandler(HttpServletRequest req, HttpMessageNotReadableException e) {
    logError(req, e);
    ApiError apiError = new ApiError("Malformed JSON request", e);
    return ResponseEntity.badRequest().body(apiError);
  }


  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFoundException(HttpServletRequest req, Exception e) {
    logError(req, e);
    return getErrorMessage(e, HttpStatus.NOT_FOUND);

  }

  private void logError(HttpServletRequest req, Exception e) {
    logger.warn(e.getMessage());
    logger.warn("RequestURI {}", req.getRequestURI());
    logger.error(ExceptionUtils.getStackTrace(e));
  }

  private ResponseEntity<ApiError> getErrorMessage(Exception e, HttpStatus status) {
    ApiError apiError = new ApiError(e);
    return ResponseEntity.status(status).body(apiError);
  }

}
