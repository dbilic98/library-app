package com.libraryapp.domain.service;

import com.libraryapp.controller.request.RequestAuthorDto;
import com.libraryapp.domain.model.Author;
import com.libraryapp.domain.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public Author findAuthorById(Long id) {
    return authorRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Author with ID " + id + " is not found"));
  }

  public Page<Author> findAllAuthors(int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return authorRepository.findAll(pageable);
  }

  public Author createAuthor(RequestAuthorDto requestAuthorDto) {
    Author createdAuthor = new Author(requestAuthorDto.firstName(), requestAuthorDto.lastName());
    return authorRepository.save(createdAuthor);
  }

  public Author updateAuthor(Long id, RequestAuthorDto requestAuthorDto) {
    Author authorToUpdate = findAuthorById(id);
    authorToUpdate.setFirstName(requestAuthorDto.firstName());
    authorToUpdate.setLastName(requestAuthorDto.lastName());
    return authorRepository.save(authorToUpdate);
  }

  public void deleteAuthor(Long id) {
    if (authorRepository.existsById(id)) {
      authorRepository.deleteById(id);
      return;
    }
    throw new RuntimeException("Author with ID " + id + " is not found");
  }
}



