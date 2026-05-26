package com.libraryapp.exception;

import com.libraryapp.exception.response.ErrorCodes;
import com.libraryapp.exception.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthorNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAuthorNotFoundException(AuthorNotFoundException e,
      HttpServletRequest request) {
    log.warn("Author not found exception, message: {}", e.getMessage());
    return buildErrorResponse(
        HttpStatus.NOT_FOUND,
        "Author error",
        ErrorCodes.AUTHOR_NOT_FOUND.getCode(),
        e.getMessage(),
        request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
      UserAlreadyExistsException e, HttpServletRequest request) {
    log.warn("User already exists exception, message: {}", e.getMessage());
    return buildErrorResponse(
        HttpStatus.CONFLICT,
        "User error",
        ErrorCodes.USER_ALREADY_EXISTS.getCode(),
        e.getMessage(),
        request);
  }

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException e,
      HttpServletRequest request) {
    log.warn("Book bot found exception, message: {}", e.getMessage());
    return buildErrorResponse(
        HttpStatus.NOT_FOUND,
        "Book error",
        ErrorCodes.BOOK_NOT_FOUND.getCode(),
        e.getMessage(),
        request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception e, HttpServletRequest request) {
    return buildErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Internal server error",
        ErrorCodes.INTERNAL_ERROR.getCode(),
        e.getMessage(),
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status, @NonNull WebRequest request) {

    String errors = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .collect(Collectors.joining(", "));

    String path = ((ServletWebRequest) request)
        .getRequest()
        .getRequestURI();

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation error",
        ErrorCodes.INVALID_FORMAT.getCode(),
        errors,
        path);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  private ResponseEntity<ErrorResponse> buildErrorResponse(
      HttpStatus status,
      String errorType,
      int errorCode,
      String message,
      HttpServletRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(status.value(), errorType, errorCode, message,
        request.getRequestURI());

    return ResponseEntity.status(status).body(errorResponse);
  }
}

