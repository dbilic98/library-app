package com.libraryapp.controller.request;

import jakarta.validation.constraints.Size;

public record UpdateBookDto(

    @Size(min = 2, message = "Name must have at least 2 characters")
    String name,

    Boolean isAvailable

) {

}
