package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.exceptions.BookExemplarNotAvailableException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
    private Book testBook;
    private Reservation testReservation;

    @BeforeEach
    public void setUp() {
        testBookExemplar = new BookExemplar();
        testBookExemplar.setId(1L);
        testBookExemplarsList = new ArrayList<>();
        testBookExemplarsList.add(testBookExemplar);
        testBook = new Book();
        testBook.setId(1L);
        testBook.getBookExemplars().add(testBookExemplar);
        testReservation = new Reservation();
        testReservation.setStartDate(LocalDate.parse("2025-03-03"));
        testReservation.setEndDate(LocalDate.parse("2025-03-06"));
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
    public void testFindAllBookExemplarsForABookPaginated() {
        Pageable testPage = PageRequest.of(0, testBook.getBookExemplars().size());
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));

        Page<BookExemplar> testBookExemplarPage = bookExemplarService.findAll(testBook.getId(), testPage);

        Assertions.assertThat(testBookExemplarPage).isNotEmpty();
    }

    @Test
    public void testFindAllBookExemplarsForABookThrowsException() {
        Pageable testPage = PageRequest.of(0, testBook.getBookExemplars().size());
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookExemplarService.findAll(testBook.getId(), testPage))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book with id %s not found".formatted(testBook.getId()));
    }

    @Test
    public void testDeleteById() {
        bookExemplarService.deleteById(testBookExemplar.getId());

        Mockito.verify(bookExemplarRepository).deleteById(testBookExemplar.getId());
    }

    @Test
    public void testFindFirstBookExemplarForReservation() {
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.of(testBookExemplar));

        BookExemplar foundBookExemplar = bookExemplarService.findFirstBookExemplarForReservation(testBook.getId(), testReservation);

        Assertions.assertThat(foundBookExemplar).isEqualTo(testBookExemplar);
        Mockito.verify(bookExemplarRepository, Mockito.times(1)).findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate());
    }

    @Test
    public void testFindFirstBookExemplarForReservationOnBookIdThrowsException() {
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookExemplarService.findFirstBookExemplarForReservation(testBook.getId(), testReservation))
                .isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(testBook.getId());
    }

    @Test
    public void testFindFirstBookForReservationOnBookExemplarThrowsException() {
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookExemplarService.findFirstBookExemplarForReservation(testBook.getId(), testReservation))
                .isInstanceOf(BookExemplarNotAvailableException.class)
                .hasMessageContaining("Book exemplar cannot be reserved for the given period");
        Mockito.verify(bookRepository, Mockito.times(1)).findById(testBook.getId());
        Mockito.verify(bookExemplarRepository, Mockito.times(1)).findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate());
    }
}
