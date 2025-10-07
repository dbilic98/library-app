package com.libraryapp.domain.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AuthorTest {

  @Test
  void whenFirstNameIsNull_ThrowsException() {
    assertThrows(NullPointerException.class, () -> new Author(1L, null, "Bilic"));
  }

  @Test
  void whenLastNameIsNull_ThrowsException() {
    assertThrows(NullPointerException.class, () -> new Author(1L, "Doris", null));
  }

  @Test
  void whenFirstNameLengthIsLessThan3_ThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> new Author(1L, "Do", "Bilic"));
  }

  @Test
  void whenLastNameLengthIsLessThan3_ThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> new Author(1L, "Doris", "Bi"));
  }
}