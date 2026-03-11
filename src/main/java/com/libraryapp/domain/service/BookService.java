package com.libraryapp.domain.service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

import com.libraryapp.controller.request.CreateBookDto;
import com.libraryapp.controller.request.UpdateBookDto;
import com.libraryapp.domain.model.Author;
import com.libraryapp.domain.model.Book;
import com.libraryapp.domain.repository.AuthorRepository;
import com.libraryapp.domain.repository.BookRepository;
import com.libraryapp.exception.AuthorNotFoundException;
import com.libraryapp.exception.BookNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;

  private final AuthorRepository authorRepository;

  public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  public Book findBookById(Long id) {
    return bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " is not found"));
  }

  public Page<Book> findAllBooks(int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return bookRepository.findAll(pageable);
  }

  public Book createBook(Long authorId, CreateBookDto createBookDto) {
    Author foundAuthor = authorRepository.findById(authorId)
        .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " is not found"));
    Book createdBook = new Book(createBookDto.name(), createBookDto.isAvailable(), foundAuthor);
    return bookRepository.save(createdBook);
  }

  public Book updateBook(Long id, UpdateBookDto updateBookDto) {
    Book bookToUpdate = bookRepository.findById(id)
        .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " is not found"));
    if (updateBookDto.name() != null) {
      bookToUpdate.setName(updateBookDto.name());
    }
    if (updateBookDto.isAvailable() != null) {
      bookToUpdate.setAvailable(updateBookDto.isAvailable());
    }
    return bookRepository.save(bookToUpdate);
  }

  public void deleteBook(Long id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
      return;
    }
    throw new BookNotFoundException("Book with ID " + id + " is not found");
  }
}
