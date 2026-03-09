package com.libraryapp.controller;

import com.libraryapp.controller.request.CreateBookDto;
import com.libraryapp.controller.request.UpdateBookDto;
import com.libraryapp.controller.response.PaginatedResponse;
import com.libraryapp.controller.response.ResponseAuthorDto;
import com.libraryapp.controller.response.ResponseBookDto;
import com.libraryapp.domain.model.Book;
import com.libraryapp.domain.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  private ResponseBookDto toResponseDto(Book book) {
    return new ResponseBookDto(
        book.getId(),
        book.getName(),
        book.isAvailable(),
        new ResponseAuthorDto(
            book.getAuthor().getId(),
            book.getAuthor().getFirstName(),
            book.getAuthor().getLastName()));
  }

  @GetMapping("/{booksId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseBookDto findBookById(@PathVariable("booksId") Long id) {
    Book book = bookService.findBookById(id);
    return toResponseDto(book);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public PaginatedResponse<ResponseBookDto> getPaginatedBooks(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    Page<Book> booksPage = bookService.findAllBook(pageNumber, pageSize);
    Page<ResponseBookDto> bookDtoPage = booksPage.map(this::toResponseDto);
    return new PaginatedResponse<>(bookDtoPage);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseBookDto createBook(@Valid @RequestBody CreateBookDto createBookDto) {
    Book createdBook = bookService.createBook(createBookDto);
    return toResponseDto(createdBook);
  }

  @PatchMapping("/{booksId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseBookDto updateBook(@PathVariable("booksId") Long id,
      @Valid @RequestBody UpdateBookDto updateBookDto) {
    Book updatedBook = bookService.updateBook(id, updateBookDto);
    return toResponseDto(updatedBook);
  }

  @DeleteMapping("/{booksId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBook(@PathVariable("booksId") Long id) {
    bookService.deleteBook(id);
  }
}
