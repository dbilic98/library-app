package com.libraryapp.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.libraryapp.controller.UserController;
import com.libraryapp.controller.request.RequestUserDto;
import com.libraryapp.domain.model.User;
import com.libraryapp.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserSecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserService userService;

  @Test
  public void createUser_whenAnonymous_returnsCreated() throws Exception {

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
        .andExpect(status().isCreated());
  }
}
