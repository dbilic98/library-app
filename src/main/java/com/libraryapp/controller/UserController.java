package com.libraryapp.controller;

import com.libraryapp.controller.request.RequestUserDto;
import com.libraryapp.controller.response.ResponseUserDto;
import com.libraryapp.domain.model.User;
import com.libraryapp.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseUserDto createUser(@Valid @RequestBody RequestUserDto requestUserDto) {
    User createdUser = userService.createUser(requestUserDto);
    return new ResponseUserDto(
        createdUser.getUsername());
  }
}