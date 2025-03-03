package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.Book;
import com.project.bookstore.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


//TODO: complete repository tests
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

}