package com.project.bookstore.unitTest.entity;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Library;
import com.project.bookstore.entity.types.BookLanguage;
import com.project.bookstore.entity.types.Genre;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class BookTests {
    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book();
    }

    @Test
    public void givenId_GetId_ReturnNotNull() {
        testBook.setId(1L);

        Assertions.assertThat(testBook.getId()).isNotNull();
    }

    @Test
    public void givenNothing_GetId_ReturnNull() {
        Assertions.assertThat(testBook.getId()).isNull();
    }

    @Test
    public void givenIsbn_GetIsbn_ReturnNotEmpty() {
        testBook.setIsbn("testIsbn");

        Assertions.assertThat(testBook.getIsbn()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetIsbn_ReturnNull() {
        Assertions.assertThat(testBook.getIsbn()).isNull();
    }

    @Test
    public void givenTitle_GetTitle_ReturnNotEmpty() {
        testBook.setTitle("testTitle");

        Assertions.assertThat(testBook.getTitle()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetTitle_ReturnNull() {
        Assertions.assertThat(testBook.getTitle()).isNull();
    }

    @Test
    public void givenAuthor_GetAuthor_ReturnNotEmpty() {
        testBook.setAuthor("testAuthor");

        Assertions.assertThat(testBook.getAuthor()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetAuthor_ReturnNull() {
        Assertions.assertThat(testBook.getAuthor()).isNull();
    }

    @Test
    public void givenAppearanceDate_GetAppearanceDate_ReturnNotNull() {
        testBook.setAppearanceDate(LocalDate.now());

        Assertions.assertThat(testBook.getAppearanceDate()).isNotNull();
    }

    @Test
    public void givenNothing_GetAppearanceDate_ReturnNull() {
        Assertions.assertThat(testBook.getAppearanceDate()).isNull();
    }

    @Test
    public void givenNrOfPages_GetNrOfPages_ReturnIsGreaterThan() {
        testBook.setNrOfPages(100);

        Assertions.assertThat(testBook.getNrOfPages()).isGreaterThan(0);
    }

    @Test
    public void givenNothing_GetNrOfPages_ReturnIsZero() {
        Assertions.assertThat(testBook.getNrOfPages()).isZero();
    }

    @Test
    public void givenGenre_GetGenre_ReturnNotNull() {
        testBook.setGenre(Genre.FANTASY);

        Assertions.assertThat(testBook.getGenre()).isNotNull();
    }

    @Test
    public void givenNothing_GetGenre_ReturnNull() {
        Assertions.assertThat(testBook.getGenre()).isNull();
    }

    @Test
    public void givenBookLanguage_GetBookLanguage_ReturnNotNull() {
        testBook.setBookLanguage(BookLanguage.ENGLISH);

        Assertions.assertThat(testBook.getBookLanguage()).isNotNull();
    }

    @Test
    public void givenNothing_GetBookLanguage_ReturnNull() {
        Assertions.assertThat(testBook.getBookLanguage()).isNull();
    }

    @Test
    public void givenLibrary_GetLibrary_ReturnNotNull() {
        testBook.setLibrary(new Library());

        Assertions.assertThat(testBook.getLibrary()).isNotNull();
    }

    @Test
    public void givenNothing_GetLibrary_ReturnNull() {
        Assertions.assertThat(testBook.getLibrary()).isNull();
    }

    @Test
    public void givenBookExemplars_GetBookExemplars_ReturnNotEmpty() {
        testBook.setBookExemplars(List.of(new BookExemplar(), new BookExemplar()));

        Assertions.assertThat(testBook.getBookExemplars()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetBookExemplars_ReturnIsEmpty() {
        Assertions.assertThat(testBook.getBookExemplars()).isEmpty();
    }
}
