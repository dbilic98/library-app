package com.libraryapp.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestAuthorDto(

    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, message = "First name must have at least 3 character")
    String firstName,

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 4, message = "Last name must have at least 4 character")
    String lastName
) {

}
