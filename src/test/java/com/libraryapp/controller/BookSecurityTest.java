package com.libraryapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.libraryapp.domain.service.BookService;
import com.libraryapp.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookSecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BookService bookService;

  @Test
  public void getBook_whenAnonymous_returnsForbidden() throws Exception {
    mockMvc.perform(get("/api/books/1"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void updateBook_whenAnonymous_returnsForbidden() throws Exception {

    String requestJson = """
        {
          "name": "Knjiga",
          "isAvailable": true
        }
        """;

    mockMvc.perform(patch("/api/books/1")
            .contentType("application/json")
            .content(requestJson))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void deleteBook_whenAnonymous_returnsForbidden() throws Exception {
    mockMvc.perform(delete("/api/books/1"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void createBook_whenAnonymous_returnsForbidden() throws Exception {
    String requestJson = """
        {
          "name": "Knjiga",
          "isAvailable": true
        }
        """;

    mockMvc.perform(post("/api/books")
            .contentType("application/json")
            .content(requestJson))
        .andExpect(status().isUnauthorized());
  }
}
