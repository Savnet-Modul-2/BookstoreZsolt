package com.project.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.bookstore.entity.types.BookLanguage;
import com.project.bookstore.entity.types.Genre;

import java.time.LocalDate;
import java.util.List;

public class BookDto {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private LocalDate appearanceDate;
    private int nrOfPages;
    private Genre genre;
    private BookLanguage bookLanguage;
    //TODO:Removable from Mapper?
    @JsonIgnore
    private LibraryDto libraryDto;
    private List<BookExemplaryDto> bookExemplars;

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

    public LibraryDto getLibraryDto() {
        return libraryDto;
    }

    public void setLibraryDto(LibraryDto libraryDto) {
        this.libraryDto = libraryDto;
    }
}
