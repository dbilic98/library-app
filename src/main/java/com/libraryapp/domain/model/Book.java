package com.libraryapp.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private boolean isAvailable;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private Author author;

  public Book() {
  }

  public Book(String name, boolean isAvailable, Author author) {
    this.name = name;
    this.isAvailable = isAvailable;
    this.author = author;
  }
}

