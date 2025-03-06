package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.Book;
import com.project.bookstore.repository.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book();
        testBook.setTitle("testTitle");
        testBook.setAuthor("testAuthor");
    }

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    public void testFindBookByTitle() {
        String testTitle = "testTitle";

        bookRepository.save(testBook);
        Page<Book> testBookPage = bookRepository.findBooks(testTitle, null, PageRequest.of(0, 1));

        Assertions.assertThat(testBookPage).isNotEmpty();
    }

    @Test
    public void testFindBookByAuthor() {
        String testAuthor = "testAuthor";

        bookRepository.save(testBook);
        Page<Book> testBookPage = bookRepository.findBooks(null, testAuthor, PageRequest.of(0, 1));

        Assertions.assertThat(testBookPage).isNotEmpty();
    }

    @Test
    public void testFindBookByReturnsEmpty() {
        Page<Book> testBookPage = bookRepository.findBooks(null, null, PageRequest.of(0, 1));

        Assertions.assertThat(testBookPage).isEmpty();
    }
}