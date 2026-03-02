package com.libraryapp.exception.response;

public record ErrorResponse(
    int httpStatusCode,
    String error,
    int errorCode,
    String message,
    String path
) {

}
