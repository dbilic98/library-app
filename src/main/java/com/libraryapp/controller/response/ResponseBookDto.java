package com.libraryapp.controller.response;

public record ResponseBookDto(

    Long id,

    String name,

    Boolean isAvailable,

    ResponseAuthorDto author
) {

}
