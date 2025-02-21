package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Library;
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

    @Test
    public void testAddBookToLibrary() {
        Library testLibrary = new Library();
        testLibrary.setId(1L);
        Mockito.when(libraryRepository.findById(testLibrary.getId())).thenReturn(Optional.of(testLibrary));
        bookService.addBookToLibrary(testLibrary.getId(), testBook);
        ArgumentCaptor<Library> libraryArgumentCaptor = ArgumentCaptor.forClass(Library.class);
        Mockito.verify(libraryRepository).save(libraryArgumentCaptor.capture());
        Library capturedLibrary = libraryArgumentCaptor.getValue();
        Assertions.assertThat(capturedLibrary.getBooks()).contains(testBook);
    }

    @Test
    public void testAddBookToWrongLibraryIdThrowsException() {
        Mockito.when(libraryRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> bookService.addBookToLibrary(Mockito.anyLong(), testBook))
                .isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(libraryRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testFindBookById() {
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Book foundBook = bookService.findBookById(testBook.getId());
        AssertionsForClassTypes.assertThat(foundBook).isEqualTo(testBook);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(testBook.getId());
    }

    @Test
    public void testFindBookByIdThrowsException() {
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> bookService.findBookById(testBook.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(testBook.getId());
    }

    @Test
    public void testFindAllBooks() {
        bookService.findAllBooks();
        Mockito.verify(bookRepository).findAll();
    }

    @Test
    public void testDeleteBookById() {
        bookService.deleteBookById(testBook.getId());
        Mockito.verify(bookRepository).deleteById(testBook.getId());
    }

    @Test
    public void testBookShouldBeUpdated() {
        Book newBook = new Book();
        newBook.setId(1L);
        newBook.setTitle("newTestTitle");
        newBook.setAuthor("newTestAuthor");
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        bookService.updateBookById(newBook.getId(), newBook);
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        Mockito.verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        AssertionsForClassTypes.assertThat(capturedBook).isEqualTo(testBook);

    }
}
