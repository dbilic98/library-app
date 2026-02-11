package com.libraryapp.exception;

import com.libraryapp.exception.response.ErrorResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AuthorNotFoundException.class)

  public ResponseEntity<Object> handleNotFoundException(Exception e, WebRequest request) {
    List<String> reasons = new ArrayList<>();
    reasons.add(e.getMessage());

    ErrorResponse errorResponse = new ErrorResponse(reasons);
    return handleExceptionInternal(
        e,
        errorResponse,
        new HttpHeaders(),
        HttpStatus.NOT_FOUND,
        request
    );
  }
}
