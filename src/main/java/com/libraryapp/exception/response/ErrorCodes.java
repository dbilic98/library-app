package com.libraryapp.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {

  //Author error 21xx
  AUTHOR_NOT_FOUND(2100, "Author not found"),

  //Book error 21xx
  BOOK_NOT_FOUND(2110, "Book not found"),

  //Validation 40xx
  VALIDATION_ERROR(4001, "Validation error"),
  REQUIRED_FIELD_MISSING(4002, "Required field missing"),
  INVALID_FORMAT(4003, "Invalid format"),

  //Server 50xx
  INTERNAL_ERROR(5001, "Internal server error");

  private final int code;
  private final String message;
}

