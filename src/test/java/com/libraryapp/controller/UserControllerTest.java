package com.libraryapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.libraryapp.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserControllerTest.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserController userController;

  @Test
  public void createUser_whenMissingUsername_returnsBadRequest() throws Exception {

    String requestJson = """
        {
        "username": "",
        "password": "Password@123"
        }
        """;
    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatusCode").value(400))
        .andExpect(jsonPath("$.error").value("Validation error"))
        .andExpect(jsonPath("$.errorCode").value(4003)).andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/api/users"));
  }

  @Test
  public void createUser_whenInvalidPassword_returnsBadRequest() throws Exception {

    String requestJson = """
        {
        "username": "Ana",
        "password": "pass"
        }
        """;
    mockMvc.perform(post("/api/users")
            .contentType("application/json")
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatusCode").value(400))
        .andExpect(jsonPath("$.error").value("Validation error"))
        .andExpect(jsonPath("$.errorCode").value(4003)).andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/api/users"));
  }
}