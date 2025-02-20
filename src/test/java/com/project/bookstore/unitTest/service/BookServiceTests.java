package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.LibraryRepository;
import com.project.bookstore.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private LibraryRepository libraryRepository;
    @InjectMocks
    private BookService bookService;
    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("testTitle");
        testBook.setAuthor("testAuthor");
    }

    @Test
    public void testBookIsCreated() {
        bookService.createBook(testBook);
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        AssertionsForClassTypes.assertThat(capturedBook).isEqualTo(testBook);
    }

    //TODO: write test for addBookToLibrary

    @Test
    public void testFindBookById() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        Book foundBook = bookService.findBookById(1L);
        AssertionsForClassTypes.assertThat(foundBook).isEqualTo(testBook);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void testFindBookByIdThrowsException() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> bookService.findBookById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(Mockito.anyString());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
    }

    //TODO: write tests for the other methods inside BookService


}
