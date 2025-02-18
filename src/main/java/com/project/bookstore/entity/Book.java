package com.project.bookstore.entity;


import com.project.bookstore.entity.types.BookLanguage;
import com.project.bookstore.entity.types.Genre;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "book")
@Table(name = "book", schema = "public")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "isbn")
    private String isbn;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "appearanceDate")
    private LocalDate appearanceDate;
    @Column(name = "nrOfPages")
    private int nrOfPages;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @Enumerated(EnumType.STRING)
    private BookLanguage bookLanguage;

    @ManyToOne()
    @JoinColumn(name = "library_id")
    private Library library;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getAppearanceDate() {
        return appearanceDate;
    }

    public void setAppearanceDate(LocalDate appearanceDate) {
        this.appearanceDate = appearanceDate;
    }

    public int getNrOfPages() {
        return nrOfPages;
    }

    public void setNrOfPages(int nrOfPages) {
        this.nrOfPages = nrOfPages;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public BookLanguage getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(BookLanguage bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
}
