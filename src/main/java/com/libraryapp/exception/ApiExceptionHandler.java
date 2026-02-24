package com.libraryapp.exception;

import com.libraryapp.exception.response.ErrorCodes;
import com.libraryapp.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthorNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(AuthorNotFoundException e,
      HttpServletRequest request) {
    return buildErrorResponse("Author error", ErrorCodes.AUTHOR_NOT_FOUND.getCode(),
        HttpStatus.NOT_FOUND,
        e.getMessage(), request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception e, HttpServletRequest request) {
    return buildErrorResponse("Internal server error", ErrorCodes.INTERNAL_ERROR.getCode(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        e.getMessage(), request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException e,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

    String errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .collect(Collectors.joining(", "));

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation error",
        ErrorCodes.INVALID_FORMAT.getCode(),
        "Request body is invalid",
        errors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      String errorType,
      int errorCode,
      HttpStatus status,
      String message,
      HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
        status.value(),
        errorType,
        errorCode,
        message,
        request.getRequestURI());

    return ResponseEntity.status(status).body(errorResponse);
  }
}

