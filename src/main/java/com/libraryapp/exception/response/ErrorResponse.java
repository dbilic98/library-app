package com.libraryapp.exception.response;

public record ErrorResponse(
    int status,
    String error,
    int errorCode,
    String message,
    String path
) {

}
