package com.libraryapp.controller;

import com.libraryapp.controller.request.RequestUserDto;
import com.libraryapp.controller.response.ResponseUserDto;
import com.libraryapp.domain.model.User;
import com.libraryapp.domain.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping
  public ResponseUserDto createUser(@Valid @RequestBody RequestUserDto requestUserDto) {
    User userToSave = new User();
    userToSave.setUsername(requestUserDto.username());
    userToSave.setPassword(passwordEncoder.encode(requestUserDto.password()));
    User createdUser = userRepository.save(userToSave);
    return new ResponseUserDto(createdUser.getUsername());
  }
}