package com.libraryapp.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestUserDto(

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, message = "Username must have at least 3 character")
    String username,

    @NotBlank(message = "Password is mandatory")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
        message = "Password must be at least 8 characters, include one uppercase, one lowercase, and one special character"
    )
    String password
) {

}
