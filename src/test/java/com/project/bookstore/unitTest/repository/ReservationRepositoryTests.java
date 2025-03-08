package com.project.bookstore.unitTest.repository;

import com.project.bookstore.entity.*;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.LibraryRepository;
import com.project.bookstore.repository.ReservationRepository;
import com.project.bookstore.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ReservationRepositoryTests {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private UserRepository userRepository;
    private Reservation testReservation;
    private BookExemplar testBookExemplar;
    private Book testBook;
    private Library testLibrary;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testReservation = new Reservation();
        testBookExemplar = new BookExemplar();
        testBook = new Book();
        testLibrary = new Library();
        testUser = new User();
        testBookExemplar.setBook(testBook);
        testBook.getBookExemplars().add(testBookExemplar);
        testLibrary.getBooks().add(testBook);
        testBook.setLibrary(testLibrary);
    }

    @AfterEach
    public void tearDown() {
        reservationRepository.deleteAll();
        libraryRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testSearchByExpiredPendingReservations() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        boolean expected = reservationRepository.searchExpiredPendingReservations(LocalDate.now().plusDays(1), ReservationStatus.PENDING).isEmpty();

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    public void testSearchByExpiredPendingReservationsWithBeforeStartDateReturnsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        boolean expected = reservationRepository.searchExpiredPendingReservations(LocalDate.now().minusDays(1), ReservationStatus.PENDING).isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchByExpiredPendingReservationsWithDifferentStatusReturnsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.CANCELLED);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        boolean expected = reservationRepository.searchExpiredPendingReservations(LocalDate.now().minusDays(1), ReservationStatus.PENDING).isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchByExpiredDelayedReservations() {
        testReservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
        testReservation.setEndDate(LocalDate.now());
        reservationRepository.save(testReservation);

        boolean expected = reservationRepository.searchExpiredDelayedReservations(LocalDate.now().plusDays(1), ReservationStatus.IN_PROGRESS).isEmpty();

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    public void testSearchByExpiredDelayedReservationWithBeforeEndDateReturnsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
        testReservation.setEndDate(LocalDate.now());
        reservationRepository.save(testReservation);

        boolean expected = reservationRepository.searchExpiredPendingReservations(LocalDate.now().minusDays(1), ReservationStatus.IN_PROGRESS).isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchByExpiredDelayedReservationsWithDifferentStatusReturnsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.CANCELLED);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        boolean expected = reservationRepository.searchExpiredPendingReservations(LocalDate.now().plusDays(1), ReservationStatus.PENDING).isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchReservationForALibraryByTimePeriod() {
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        reservationRepository.save(testReservation);
        libraryRepository.save(testLibrary);

        Library foundLibrary = libraryRepository.findAll().get(0);
        boolean expected = reservationRepository.searchReservationsForALibraryByTimePeriod(
                        foundLibrary.getId(),
                        LocalDate.now().minusDays(3),
                        LocalDate.now().plusDays(10),
                        PageRequest.of(0, 1))
                .isEmpty();

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    public void testSearchReservationsForALibraryByTimePeriodWhenPeriodsDontMatch() {
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        reservationRepository.save(testReservation);
        libraryRepository.save(testLibrary);

        Library foundLibrary = libraryRepository.findAll().get(0);
        boolean expected = reservationRepository.searchReservationsForALibraryByTimePeriod(
                        foundLibrary.getId(),
                        LocalDate.now().minusDays(3),
                        LocalDate.now(),
                        PageRequest.of(0, 1))
                .isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchReservationsForALibraryByTimePeriodWhenStatusDoesntMatch() {
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(testReservation);
        libraryRepository.save(testLibrary);

        Library foundLibrary = libraryRepository.findAll().get(0);
        boolean expected = reservationRepository.searchReservationsForALibraryByTimePeriod(
                        foundLibrary.getId(),
                        LocalDate.now().minusDays(3),
                        LocalDate.now().plusDays(10),
                        PageRequest.of(0, 1))
                .isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchReservationsForAUserByReservationStatus() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        User foundUser = userRepository.findAll().get(0);
        boolean expected = reservationRepository.searchReservationsForAUserByReservationStatus(
                        foundUser.getId(),
                        ReservationStatus.PENDING,
                        PageRequest.of(0, 1))
                .isEmpty();

        Assertions.assertThat(expected).isFalse();
    }

    @Test
    public void testSearchReservationsForAUserByReservationStatusWhenUserDoesntExits() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        boolean expected = reservationRepository.searchReservationsForAUserByReservationStatus(
                        10L,
                        ReservationStatus.PENDING,
                        PageRequest.of(0, 1))
                .isEmpty();

        Assertions.assertThat(expected).isTrue();
    }

    @Test
    public void testSearchReservationsForAUserByReservationStatusWhenStatusIsWrong() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        User foundUser = userRepository.findAll().get(0);
        boolean expected = reservationRepository.searchReservationsForAUserByReservationStatus(
                        foundUser.getId(),
                        ReservationStatus.IN_PROGRESS,
                        PageRequest.of(0, 1))
                .isEmpty();

        Assertions.assertThat(expected).isTrue();
    }
}
