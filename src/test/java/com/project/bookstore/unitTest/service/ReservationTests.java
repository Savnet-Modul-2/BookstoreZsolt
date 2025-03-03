package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.BookExemplarNotAvailableException;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.ReservationRepository;
import com.project.bookstore.repository.UserRepository;
import com.project.bookstore.service.ReservationService;
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

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReservationTests {
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookExemplarRepository bookExemplarRepository;
    @InjectMocks
    private ReservationService reservationService;
    private Reservation testReservation;
    private User testUser;
    private Book testBook;
    private BookExemplar testBookExemplar;

    @BeforeEach
    public void setUp() {
        testReservation = new Reservation();
        testReservation.setStartDate(LocalDate.parse("2025-03-03"));
        testReservation.setEndDate(LocalDate.parse("2025-03-06"));
        testUser = new User();
        testUser.setId(1L);
        testBook = new Book();
        testBook.setId(1L);
        testBookExemplar = new BookExemplar();
        testBookExemplar.setId(1L);
        testBook.getBookExemplars().add(testBookExemplar);
    }

    @Test
    public void testReserveBook() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.of(testBookExemplar));

        reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation);
        ArgumentCaptor<Reservation> reservationArgumentCaptor = ArgumentCaptor.forClass(Reservation.class);
        Mockito.verify(reservationRepository).save(reservationArgumentCaptor.capture());
        Reservation capturedReservation = reservationArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(capturedReservation).isEqualTo(testReservation);
    }

    @Test
    public void testReserveBookWhenUserNotFoundThrowsException() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id %s not found".formatted(testUser.getId()));
    }

    @Test
    public void testReserveBookWhenBookNotFoundThrowsException() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book with id %s not found".formatted(testBook.getId()));
    }

    @Test
    public void testReserveBookWhenExemplarNotAvailableThrowsException(){
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(BookExemplarNotAvailableException.class)
                .hasMessageContaining("Book exemplar cannot be reserved for the given period");
    }
}
