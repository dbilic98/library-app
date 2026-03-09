package com.libraryapp.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBookDto(

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "Name must have at least 2 characters")
    String name,

    @NotNull(message = "Availability must be provided")
    Boolean isAvailable,

    @NotNull(message = "Author is required")
    Long authorId

) {

}
