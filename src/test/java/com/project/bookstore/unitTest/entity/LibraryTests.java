package com.project.bookstore.unitTest.entity;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Librarian;
import com.project.bookstore.entity.Library;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LibraryTests {
    private Library testLibrary;

    @BeforeEach
    public void setUp() {
        testLibrary = new Library();
    }

    @Test
    public void givenId_GetId_ReturnNotNull() {
        testLibrary.setId(1L);

        Assertions.assertThat(testLibrary.getId()).isNotNull();
    }

    @Test
    public void givenNothing_GetId_ReturnNull() {
        Assertions.assertThat(testLibrary.getId()).isNull();
    }

    @Test
    public void givenName_GetName_ReturnNotEmpty() {
        testLibrary.setName("testName");

        Assertions.assertThat(testLibrary.getName()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetName_ReturnNull() {
        Assertions.assertThat(testLibrary.getName()).isNull();
    }

    @Test
    public void givenCity_GetCity_ReturnNotEmpty() {
        testLibrary.setCity("testCity");

        Assertions.assertThat(testLibrary.getCity()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetCity_ReturnNull() {
        Assertions.assertThat(testLibrary.getCity()).isNull();
    }

    @Test
    public void givePhoneNumber_GetPhoneNumber_ReturnNotEmpty() {
        testLibrary.setPhoneNumber("123456789");

        Assertions.assertThat(testLibrary.getPhoneNumber()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetPhoneNumber_ReturnNull() {
        Assertions.assertThat(testLibrary.getPhoneNumber()).isNull();
    }

    @Test
    public void givenLibrarian_GetLibrarian_ReturnNotNull() {
        testLibrary.setLibrarian(new Librarian());

        Assertions.assertThat(testLibrary.getLibrarian()).isNotNull();
    }

    @Test
    public void givenNothing_GetLibrarian_ReturnNull() {
        Assertions.assertThat(testLibrary.getLibrarian()).isNull();
    }

    @Test
    public void givenBookList_GetBooks_ReturnNotEmpty() {
        testLibrary.setBooks(List.of(new Book()));

        Assertions.assertThat(testLibrary.getBooks()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetBooks_ReturnIsEmpty() {
        Assertions.assertThat(testLibrary.getBooks()).isEmpty();
    }

    @Test
    public void givenBook_AddBook_ReturnContainsBook() {
        Book book = new Book();
        testLibrary.addBook(book);

        Assertions.assertThat(testLibrary.getBooks()).contains(book);
        Assertions.assertThat(book.getLibrary()).isEqualTo(testLibrary);
    }
}
