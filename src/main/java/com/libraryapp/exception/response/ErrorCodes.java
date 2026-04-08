package com.libraryapp.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {

  //Author error 21xx
  AUTHOR_NOT_FOUND(2100, "Author not found"),
  USER_ALREADY_EXISTS(2102, "User already exists"),

  //Validation 40xx
  VALIDATION_ERROR(4001, "Validation error"),
  REQUIRED_FIELD_MISSING(4002, "Required field missing"),
  INVALID_FORMAT(4003, "Invalid format"),
  CONSTRAINT_VIOLATION(4004, "Constraint violation"),

  //Server 50xx
  INTERNAL_ERROR(5001, "Internal server error");

  private final int code;
  private final String message;
}

