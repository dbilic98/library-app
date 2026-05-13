package com.libraryapp.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.libraryapp.controller.request.CreateBookDto;
import com.libraryapp.controller.request.UpdateBookDto;
import com.libraryapp.domain.model.Author;
import com.libraryapp.domain.model.Book;
import com.libraryapp.domain.service.BookService;
import com.libraryapp.exception.AuthorNotFoundException;
import com.libraryapp.exception.BookNotFoundException;
import com.libraryapp.security.SecurityConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private BookService bookService;

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void getBookById_whenBookExists_returnsBook() throws Exception {
    Author author = new Author(1L, "Ana", "Anic");
    Book book = new Book("Bob", true, author);
    book.setId(1L);
    Mockito.when(bookService.findBookById(1L)).thenReturn(book);
    mockMvc.perform(get("/api/books/1").with(httpBasic("Ana", "password123")))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Bob")).andExpect(jsonPath("$.isAvailable").value(true))
        .andExpect(jsonPath("$.author.id").value(1))
        .andExpect(jsonPath("$.author.firstName").value("Ana"))
        .andExpect(jsonPath("$.author.lastName").value("Anic"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void getBookById_whenBookDoesNotExist_returnsNotFound() throws Exception {
    BookNotFoundException bookNotFoundException = new BookNotFoundException(
        "Book with ID 1 is not found");

    Mockito.when(bookService.findBookById(1L)).thenThrow(bookNotFoundException);
    mockMvc.perform(get("/api/books/1").with(httpBasic("Ana", "password123")))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.httpStatusCode").value(404))
        .andExpect(jsonPath("$.error").value("Book error"))
        .andExpect(jsonPath("$.errorCode").value(2110))
        .andExpect(jsonPath("$.message").value(bookNotFoundException.getMessage()))
        .andExpect(jsonPath("$.path").value("/api/books/1"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void getPaginatedBooks_returnsPageOfBooks() throws Exception {
    Author author = new Author(1L, "Ana", "Anic");
    Book book1 = new Book("Bob", true, author);
    Book book2 = new Book("Tob", true, author);
    Book book3 = new Book("Kob", true, author);

    List<Book> bookList = List.of(book1, book2, book3);
    Pageable pageable = PageRequest.of(0, 10);
    Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

    Mockito.when(bookService.findAllBooks(0, 10)).thenReturn(bookPage);

    mockMvc.perform(get("/api/books").with(httpBasic("Ana", "password123")).param("pageNumber", "0")
            .param("pageSize", "10")).andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(3))
        .andExpect(jsonPath("$.items[0].name").value("Bob"))
        .andExpect(jsonPath("$.items[1].name").value("Tob"))
        .andExpect(jsonPath("$.items[2].name").value("Kob"))
        .andExpect(jsonPath("$.totalItems").value(3));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void getPaginatedBooks_whenNoBooks_returnsEmptyPage() throws Exception {

    List<Book> bookList = List.of();
    Pageable pageable = PageRequest.of(0, 10);
    Page<Book> emptyPage = new PageImpl<>(bookList, pageable, 0);

    Mockito.when(bookService.findAllBooks(0, 10)).thenReturn(emptyPage);

    mockMvc.perform(get("/api/books").with(httpBasic("Ana", "password123")).param("pageNumber", "0")
            .param("pageSize", "10")).andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(0))
        .andExpect(jsonPath("$.totalItems").value(0));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void createBook_whenValidRequest_returnsCreatedBook() throws Exception {
    String requestJson = """
        {
          "name": "Nova knjiga",
          "isAvailable": true
        }
        """;
    Author author = new Author(1L, "Ana", "Anic");
    Book savedBook = new Book("Nova knjiga", true, author);
    savedBook.setId(1L);

    Mockito.when(bookService.createBook(Mockito.anyLong(), Mockito.any(CreateBookDto.class)))
        .thenReturn(savedBook);
    mockMvc.perform(post("/api/books/authors/1").with(httpBasic("Ana", "password123"))
            .contentType("application/json").content(requestJson)).andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Nova knjiga"))
        .andExpect(jsonPath("$.isAvailable").value(true))
        .andExpect(jsonPath("$.author.id").value(1))
        .andExpect(jsonPath("$.author.firstName").value("Ana"))
        .andExpect(jsonPath("$.author.lastName").value("Anic"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void createBook_whenAuthorDoesNotExist_returnsNotFound() throws Exception {
    AuthorNotFoundException authorNotFoundException = new AuthorNotFoundException(
        "Author with ID 1 is not found");

    Mockito.when(bookService.createBook(Mockito.anyLong(), Mockito.any(CreateBookDto.class)))
        .thenThrow(authorNotFoundException);

    String requestJson = """
        {
        "name": "Nova knjiga",
        "isAvailable": true
        }
        """;

    mockMvc.perform(post("/api/books/authors/1").with(httpBasic("Ana", "password123"))
            .contentType("application/json").content(requestJson)).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.httpStatusCode").value(404))
        .andExpect(jsonPath("$.error").value("Author error"))
        .andExpect(jsonPath("$.errorCode").value(2100))
        .andExpect(jsonPath("$.message").value(authorNotFoundException.getMessage()))
        .andExpect(jsonPath("$.path").value("/api/books/authors/1"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void createBook_whenInvalidRequest_returnsBadRequest() throws Exception {

    String requestJson = """
        {
        "name": "",
        "isAvailable": true
        }
        """;

    mockMvc.perform(post("/api/books/authors/1").with(httpBasic("Ana", "password123"))
            .contentType("application/json").content(requestJson)).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatusCode").value(400))
        .andExpect(jsonPath("$.error").value("Validation error"))
        .andExpect(jsonPath("$.errorCode").value(4003)).andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/api/books/authors/1"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void updateBook_whenValidPatchRequest_returnsUpdatedBook() throws Exception {

    String requestJson = """
        {
        "name": "Knjiga",
        "isAvailable": true
        }
        """;

    Author author = new Author(1L, "Ana", "Anic");
    Book updatedBook = new Book("Knjiga", true, author);
    updatedBook.setId(1L);

    Mockito.when(bookService.updateBook(Mockito.anyLong(), Mockito.any(UpdateBookDto.class)))
        .thenReturn(updatedBook);

    mockMvc.perform(
            patch("/api/books/1").with(httpBasic("Ana", "password123")).contentType("application/json")
                .content(requestJson)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Knjiga"))
        .andExpect(jsonPath("$.isAvailable").value(true))
        .andExpect(jsonPath("$.author.id").value(1))
        .andExpect(jsonPath("$.author.firstName").value("Ana"))
        .andExpect(jsonPath("$.author.lastName").value("Anic"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void updateBook_whenBookDoesNotExist_returnsNotFound() throws Exception {

    BookNotFoundException bookNotFoundException = new BookNotFoundException(
        "Book with ID 1 is not found");

    Mockito.when(bookService.updateBook(Mockito.anyLong(), Mockito.any(UpdateBookDto.class)))
        .thenThrow(bookNotFoundException);

    String requestJson = """
        {
          "name": "Updated Book",
          "isAvailable": true
        }
        """;

    mockMvc.perform(
            patch("/api/books/1").with(httpBasic("Ana", "password123")).contentType("application/json")
                .content(requestJson)).andExpect(status().isNotFound())
        .andExpect(jsonPath("$.httpStatusCode").value(404))
        .andExpect(jsonPath("$.error").value("Book error"))
        .andExpect(jsonPath("$.errorCode").value(2110))
        .andExpect(jsonPath("$.message").value(bookNotFoundException.getMessage()))
        .andExpect(jsonPath("$.path").value("/api/books/1"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void updateBook_whenInvalidRequest_returnsBadRequest() throws Exception {

    String requestJson = """
        {
        "name": "A",
        "isAvailable": true
        }
        """;

    mockMvc.perform(
            patch("/api/books/1").with(httpBasic("Ana", "password123")).contentType("application/json")
                .content(requestJson)).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatusCode").value(400))
        .andExpect(jsonPath("$.error").value("Validation error"))
        .andExpect(jsonPath("$.errorCode").value(4003)).andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.path").value("/api/books/1"));
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void deleteBook_whenBookExists_returnsNoContent() throws Exception {

    Long bookId = 1L;

    doNothing().when(bookService).deleteBook(bookId);

    mockMvc.perform(delete("/api/books/{bookId}", bookId).with(httpBasic("Ana", "password123")))
        .andExpect(status().isNoContent());

    verify(bookService).deleteBook(bookId);
  }

  @Test
  @WithMockUser(username = "Ana", roles = "USER")
  public void deleteBook_whenBookDoesNotExist_returnsBookNotFound() throws Exception {

    BookNotFoundException bookNotFoundException = new BookNotFoundException(
        "Book with ID 1 is not found");

    Mockito.doThrow(bookNotFoundException).when(bookService).deleteBook(Mockito.anyLong());

    mockMvc.perform(delete("/api/books/1").with(httpBasic("Ana", "password123")))
        .andExpect(status().isNotFound()).andExpect(jsonPath("$.httpStatusCode").value(404))
        .andExpect(jsonPath("$.error").value("Book error"))
        .andExpect(jsonPath("$.errorCode").value(2110))
        .andExpect(jsonPath("$.message").value("Book with ID 1 is not found"))
        .andExpect(jsonPath("$.path").value("/api/books/1"));
  }
}