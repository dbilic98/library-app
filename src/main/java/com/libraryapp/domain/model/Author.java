package com.libraryapp.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "author")
@Getter
@Setter
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;

  private String lastName;

  public Author() {
  }

  public Author(Long id, String firstName, String lastName) {
    Objects.requireNonNull(firstName, "firstName is mandatory");
    Objects.requireNonNull(lastName, "lastName is mandatory");

    if (firstName.length() < 3) {
      throw new IllegalArgumentException("firstName must be at least 3 characters");
    }
    if (lastName.length() < 4) {
      throw new IllegalArgumentException("lastName must be at least 4 characters");
    }
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Author(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
