package com.libraryapp.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.libraryapp.domain.model.Author;
import com.libraryapp.domain.model.Book;
import com.libraryapp.domain.repository.BookRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  @Test
  void shouldReturnBookWhenIdExists() {

    //ARRANGE
    Book mockBook = new Book("Moc nesavrsenstva", true, new Author());

    when(bookRepository.findById(1L))
        .thenReturn(Optional.of(mockBook));

    //ACT
    Book result = bookService.findBookById(1L);

    //ASSERT
    assertEquals("Moc nesavrsenstva", result.getName());
    assertTrue(result.isAvailable());

    verify(bookRepository).findById(1L);
  }
}