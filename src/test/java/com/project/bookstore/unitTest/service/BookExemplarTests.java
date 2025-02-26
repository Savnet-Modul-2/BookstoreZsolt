package com.project.bookstore.unitTest.service;


import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.mapper.BookExemplarMapper;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.service.BookExemplarService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class BookExemplarTests {
    @Mock
    private BookExemplarRepository bookExemplarRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookExemplarService bookExemplarService;
    private BookExemplar testBookExemplar;
    private List<BookExemplar> testBookExemplarsList;

    @BeforeEach
    public void setUp() {
        testBookExemplar = new BookExemplar();
        testBookExemplar.setId(1L);
        testBookExemplarsList = new ArrayList<>();
        testBookExemplarsList.add(testBookExemplar);
    }

    @Test
    public void testBookExemplarIsCreated() {
        Book testBook = new Book();
        testBook.setId(1L);
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.saveAll(testBookExemplarsList)).thenReturn(testBookExemplarsList);

        List<BookExemplar> savedBookExemplars = bookExemplarService.createBookExemplars(testBook.getId(), testBookExemplarsList);

        Assertions.assertThat(savedBookExemplars).isEqualTo(testBookExemplarsList);

        Mockito.verify(bookExemplarRepository, Mockito.times(1)).saveAll(testBookExemplarsList);
    }

    @Test
    public void testBookExemplarIsCreatedThrowsException() {
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookExemplarService.createBookExemplars(Mockito.anyLong(), testBookExemplarsList))
                .isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(bookExemplarRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testFindAllBookExemplars() {
        bookExemplarService.findAll();

        Mockito.verify(bookExemplarRepository).findAll();
    }

    @Test
    public void testFindAllBookExemplarsPaginated() {
        Pageable testPage = PageRequest.of(0, testBookExemplarsList.size());
        Mockito.when(bookExemplarRepository.findAll(testPage))
                .thenReturn(new PageImpl<>(testBookExemplarsList, testPage, testBookExemplarsList.size()));

        bookExemplarService.findAll(testPage);

        Mockito.verify(bookExemplarRepository).findAll(testPage);
    }

    @Test
    public void testDeleteById() {
        bookExemplarService.deleteById(testBookExemplar.getId());

        Mockito.verify(bookExemplarRepository).deleteById(testBookExemplar.getId());
    }
}
