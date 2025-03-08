package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.ReservationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookExemplarRepositoryTests {
    @Autowired
    private BookExemplarRepository bookExemplarRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    private BookExemplar testBookExemplar;
    private Reservation testReservation;
    private Book testBook;

    @BeforeEach
    public void setUp() {
        testReservation = new Reservation();
        testBookExemplar = new BookExemplar();
        testBook = new Book();
        testBook.getBookExemplars().add(testBookExemplar);
        testBookExemplar.setBook(testBook);
    }

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Test
    public void testFindFirstExemplarAvailable() {
        bookRepository.save(testBook);
        Book testBookForId = bookRepository.findAll().get(0);
        boolean expected = bookExemplarRepository.findFirstExemplarAvailable(testBookForId.getId(),
                        LocalDate.of(2025, 3, 5),
                        LocalDate.of(2025, 3, 7))
                .isPresent();
        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testFindFirstExemplarAvailableWhenBookIdDoesntExist() {
        boolean expected = bookExemplarRepository.findFirstExemplarAvailable(1L,
                        LocalDate.of(2025, 3, 5),
                        LocalDate.of(2025, 3, 7))
                .isPresent();

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    public void testFindFirstExemplarAvailableWhenDatesDontMatch() {
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setStartDate(LocalDate.of(2025, 3, 5));
        testReservation.setEndDate(LocalDate.of(2025, 3, 7));
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testBookExemplar.getReservations().add(testReservation);
        bookRepository.save(testBook);
        Book testBookForId = bookRepository.findAll().get(0);
        reservationRepository.save(testReservation);
        boolean expected = bookExemplarRepository.findFirstExemplarAvailable(testBookForId.getId(),
                        LocalDate.of(2025, 3, 5),
                        LocalDate.of(2025, 3, 6))
                .isPresent();
        Assertions.assertThat(expected).isFalse();
    }
}
