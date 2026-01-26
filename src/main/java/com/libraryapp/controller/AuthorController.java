package com.libraryapp.controller;

import com.libraryapp.controller.request.RequestAuthorDto;
import com.libraryapp.controller.response.PaginatedResponse;
import com.libraryapp.controller.response.ResponseAuthorDto;
import com.libraryapp.domain.model.Author;
import com.libraryapp.domain.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/author")
public class AuthorController {

  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping("/{id}")
  public ResponseAuthorDto findAuthorById(@PathVariable("id") Long id) {
    Author author = authorService.findAuthorById(id);
    return new ResponseAuthorDto(
        author.getId(),
        author.getFirstName(),
        author.getLastName());
  }

  @GetMapping
  public PaginatedResponse<ResponseAuthorDto> findAllAuthors(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    Page<Author> allAuthors = authorService.findAllAuthors(pageNumber, pageSize);
    Page<ResponseAuthorDto> map = allAuthors.map(this::toResponseDto);
    return new PaginatedResponse<>(map);
  }

  private ResponseAuthorDto toResponseDto(Author author) {
    return new ResponseAuthorDto(
        author.getId(),
        author.getFirstName(),
        author.getLastName());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseAuthorDto createAuthor(@Valid @RequestBody RequestAuthorDto requestAuthorDto) {
    Author createdAuthor = authorService.createAuthor(requestAuthorDto);
    return new ResponseAuthorDto(
        createdAuthor.getId(),
        createdAuthor.getFirstName(),
        createdAuthor.getLastName());
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseAuthorDto updateAuthor(@Valid @PathVariable("id") Long id,
      @RequestBody RequestAuthorDto requestAuthorDto) {
    Author updatedAuthor = authorService.updateAuthor(id, requestAuthorDto);
    return new ResponseAuthorDto(
        updatedAuthor.getId(),
        updatedAuthor.getFirstName(),
        updatedAuthor.getLastName());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAuthor(@PathVariable("id") Long id) {
    authorService.deleteAuthor(id);
  }
}
