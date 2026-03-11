package com.libraryapp.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUserDto(

    @NotBlank(message = "User is mandatory")
    @Size(min = 3, message = "Username must have at least 3 character")
    String username,

    @NotBlank(message = "Password is mandatory")
    @Size(min = 3, message = "Password must have at least 8 character")
    String password
) {

}
