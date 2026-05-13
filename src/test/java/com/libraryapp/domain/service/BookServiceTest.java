package com.libraryapp.domain.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.libraryapp.controller.request.CreateBookDto;
import com.libraryapp.controller.request.UpdateBookDto;
import com.libraryapp.domain.model.Author;
import com.libraryapp.domain.model.Book;
import com.libraryapp.domain.repository.AuthorRepository;
import com.libraryapp.domain.repository.BookRepository;
import com.libraryapp.exception.AuthorNotFoundException;
import com.libraryapp.exception.BookNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

  @Mock
  private BookRepository bookRepository;

  @Mock
  private AuthorRepository authorRepository;

  @InjectMocks
  private BookService bookService;

  @Test
  void shouldReturnBook_whenBookExists() {

    //ARRANGE
    Author author = new Author(1L, "Ana", "Anic");
    Book existingBook = new Book("Book 1", true, author);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

    //ACT
    Book foundBook = bookService.findBookById(1L);

    //ASSERT
    assertAll(
        () -> assertEquals("Book 1", foundBook.getName()),
        () -> assertTrue(foundBook.isAvailable()),
        () -> assertNotNull(foundBook.getAuthor()));

    verify(bookRepository).findById(1L);
  }

  @Test
  void shouldThrowBookNotFoundException_whenBookDoesNotExist() {

    //ARRANGE
    when(bookRepository.findById(99L)).thenReturn(Optional.empty());

    //ACT + ASSERT
    BookNotFoundException exception = assertThrows(BookNotFoundException.class,
        () -> bookService.findBookById(99L));

    assertEquals("Book with ID 99 is not found", exception.getMessage());

    verify(bookRepository).findById(99L);
  }

  @Test
  void shouldThrowAuthorNotFoundException_whenAuthorDoesNotExist() {

    when(authorRepository.findById(1L)).thenReturn(Optional.empty());

    AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class,
        () -> bookService.createBook(1L, new CreateBookDto("Bob", true)));

    assertEquals("Author with ID 1 is not found", exception.getMessage());

    verify(authorRepository).findById(1L);
  }

  @Test
  void shouldReturnPageOfBooks() {

    int pageNumber = 0;
    int pageSize = 2;

    Author author = new Author(1L, "Ana", "Anic");
    List<Book> bookList = List.of(
        new Book("Book 1", true, author),
        new Book("Book 2", true, author));

    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

    when(bookRepository.findAll(pageable)).thenReturn(bookPage);

    Page<Book> foundAllBooks = bookService.findAllBooks(pageNumber, pageSize);

    assertEquals(2, foundAllBooks.getContent().size());
    assertEquals("Book 1", foundAllBooks.getContent().getFirst().getName());

    List<Book> content = foundAllBooks.getContent();
    Book book = content.get(1);
    String name = book.getName();

    assertEquals("Book 2", name);

    verify(bookRepository).findAll(pageable);
  }

  @Test
  void shouldCreateBookSuccessfully() {

    Author author = new Author(1L, "Ana", "Anic");
    Book savedBook = new Book("Bob", true, author);

    when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
    when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

    Book createdBook = bookService.createBook(1L, new CreateBookDto("Bob", true));

    assertEquals("Bob", createdBook.getName());
    assertEquals(author, createdBook.getAuthor());

    verify(authorRepository).findById(1L);
    verify(bookRepository).save(any(Book.class));
  }

  @Test
  void shouldUpdateAllFields() {

    Author author = new Author(1L, "Ana", "Anic");
    Book existingBook = new Book("Book 1", false, author);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
    when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

    Book updatedBook = bookService.updateBook(1L, new UpdateBookDto("Book 2", true));

    assertEquals("Book 2", updatedBook.getName());
    assertTrue(updatedBook.isAvailable());

    verify(bookRepository).findById(1L);
    verify(bookRepository).save(any(Book.class));
  }

  @Test
  void shouldUpdateOnlyName() {

    Author author = new Author(1L, "Ana", "Anic");
    Book existingBook = new Book("Old name", true, author);

    when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
    when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

    Book updatedBook = bookService.updateBook(1L, new UpdateBookDto("New name", null));

    assertEquals("New name", updatedBook.getName());
    assertTrue(updatedBook.isAvailable());

    verify(bookRepository).save(any(Book.class));
  }

  @Test
  void shouldThrowBookNotFoundException_whenUpdating() {

    when(bookRepository.findById(1L)).thenReturn(Optional.empty());

    BookNotFoundException exception = assertThrows(BookNotFoundException.class,
        () -> bookService.updateBook(1L, new UpdateBookDto("Book 1", true)));

    assertEquals("Book with ID 1 is not found", exception.getMessage());

    verify(bookRepository, never()).save(any());
  }

  @Test
  void shouldDeleteBook() {

    when(bookRepository.existsById(1L)).thenReturn(true);

    bookService.deleteBook(1L);

    verify(bookRepository).deleteById(1L);
  }

  @Test
  void shouldThrowException_whenDeletingBookNotFound() {

    when(bookRepository.existsById(1L)).thenReturn(false);

    BookNotFoundException exception = assertThrows(BookNotFoundException.class,
        () -> bookService.deleteBook(1L));

    assertEquals("Book with ID 1 is not found", exception.getMessage());

    verify(bookRepository, never()).deleteById(any());
  }
}