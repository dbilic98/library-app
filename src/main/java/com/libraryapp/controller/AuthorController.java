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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  private ResponseAuthorDto toResponseDto(Author author) {
    return new ResponseAuthorDto(
        author.getId(),
        author.getFirstName(),
        author.getLastName());
  }

  @GetMapping("/{authorsId}")
  public ResponseAuthorDto getAuthorById(@PathVariable("authorsId") Long id) {
    Author author = authorService.findAuthorById(id);
    return new ResponseAuthorDto(
        author.getId(),
        author.getFirstName(),
        author.getLastName());
  }

  @GetMapping
  public PaginatedResponse<ResponseAuthorDto> getPaginatedAuthors(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "10") int pageSize) {
    Page<Author> authorsPage = authorService.findAllAuthors(pageNumber, pageSize);
    Page<ResponseAuthorDto> authorsDtoPage = authorsPage.map(this::toResponseDto);
    return new PaginatedResponse<>(authorsDtoPage);
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

  @PatchMapping("/{authorsId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseAuthorDto patchAuthor(@Valid @PathVariable("authorsId") Long id,
      @RequestBody RequestAuthorDto requestAuthorDto) {
    Author updatedAuthor = authorService.updateAuthor(id, requestAuthorDto);
    return new ResponseAuthorDto(
        updatedAuthor.getId(),
        updatedAuthor.getFirstName(),
        updatedAuthor.getLastName());
  }

  @DeleteMapping("/{authorsId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAuthor(@PathVariable("authorsId") Long id) {
    authorService.deleteAuthor(id);
  }
}
