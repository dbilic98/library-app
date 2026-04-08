package com.libraryapp.domain.service;

import com.libraryapp.controller.request.RequestUserDto;
import com.libraryapp.domain.model.User;
import com.libraryapp.domain.repository.UserRepository;
import com.libraryapp.exception.UserAlreadyExistsException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .orElseThrow(
            () -> new UsernameNotFoundException("User with username " + username + " not found"));
  }

  public User createUser(RequestUserDto requestUserDto) {

    if (userRepository.findByUsername(requestUserDto.username()).isPresent()) {
      throw new UserAlreadyExistsException("Username already exists");
    }
    User createdUser = new User(
        requestUserDto.username(),
        passwordEncoder.encode(requestUserDto.password()));

    return userRepository.save(createdUser);
  }

  public void readFile(String path) throws FileNotFoundException {
    FileReader fileName = new FileReader(path);
  }
}
