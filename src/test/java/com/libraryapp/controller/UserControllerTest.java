package com.libraryapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.libraryapp.controller.request.RequestUserDto;
import com.libraryapp.domain.model.User;
import com.libraryapp.domain.service.UserService;
import com.libraryapp.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"An"})
  public void createUser_whenUsernameInvalid_returnsBadRequest(String username) throws Exception {

    String requestJson = """
        {
        "username": %s,
        "password": "Password@123"
        }
        """.formatted(
        username == null ? "null" : "\"" + username + "\""
    );

    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(requestJson))

        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatusCode").value(400))
        .andExpect(jsonPath("$.error").value("Validation error"))
        .andExpect(jsonPath("$.errorCode").value(4003))
        .andExpect(jsonPath("$.message").exists());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {
      "pass",
      "password",
      "PASSWORD",
      "Password",
      "Password1",
      "password!"
  })
  public void createUser_whenInvalidPassword_returnsBadRequest(String password) throws Exception {

    String requestJson = """
        {
        "username": "Ana",
        "password": %s
        }
        """.formatted(
        password == null ? "null" : "\"" + password + "\""
    );

    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(requestJson))

        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatusCode").value(400))
        .andExpect(jsonPath("$.error").value("Validation error"))
        .andExpect(jsonPath("$.errorCode").value(4003))
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/api/users"));
  }

  @Test
  public void createUser_whenValidRequest_returnsCreated() throws Exception {

    User user = new User("Ana", "Password@123");

    Mockito.when(userService.createUser(Mockito.any(RequestUserDto.class))).thenReturn(user);

    String requestJson = """
        {
        "username": "Ana",
        "password": "Password@123"
        }
        """;

    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(requestJson))

        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("Ana"));
  }
}