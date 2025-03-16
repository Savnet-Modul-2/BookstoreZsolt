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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

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
    public void givenStartDateAndStatus_FindByStartDateOlderThanAndStatus_ReturnNotEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        List<Reservation> expected = reservationRepository.findByStartDateOlderThanAndStatus(LocalDate.now().plusDays(1), ReservationStatus.PENDING);

        Assertions.assertThat(expected).isNotEmpty();
    }

    @Test
    public void givenWrongStartDate_FindByStartDateOlderThanAndStatus_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        List<Reservation> expected = reservationRepository.findByStartDateOlderThanAndStatus(LocalDate.now().minusDays(1), ReservationStatus.PENDING);

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenWrongStatus_FindByStartDateOlderThanAndStatus_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.CANCELLED);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        List<Reservation> expected = reservationRepository.findByStartDateOlderThanAndStatus(LocalDate.now().plusDays(1), ReservationStatus.PENDING);

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenEndDateAndStatus_FindByEndDateOlderThanAndStatus_ReturnNotEmpty() {
        testReservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
        testReservation.setEndDate(LocalDate.now());
        reservationRepository.save(testReservation);

        List<Reservation> expected = reservationRepository.findByEndDateOlderThanAndStatus(LocalDate.now().plusDays(1), ReservationStatus.IN_PROGRESS);

        Assertions.assertThat(expected).isNotEmpty();
    }

    @Test
    public void givenWrongEndDate_FindByEndDateOlderThanAndStatus_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
        testReservation.setEndDate(LocalDate.now());
        reservationRepository.save(testReservation);

        List<Reservation> expected = reservationRepository.findByEndDateOlderThanAndStatus(LocalDate.now().minusDays(1), ReservationStatus.IN_PROGRESS);

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenWrongStatus_FindByEndDateOlderThanAndStatus_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.CANCELLED);
        testReservation.setStartDate(LocalDate.now());
        reservationRepository.save(testReservation);

        List<Reservation> expected = reservationRepository.findByEndDateOlderThanAndStatus(LocalDate.now().plusDays(1), ReservationStatus.PENDING);

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenLibraryIdAndTimePeriod_findReservationsForALibraryByCriteria_ReturnNotEmpty() {
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        reservationRepository.save(testReservation);
        libraryRepository.save(testLibrary);

        Library foundLibrary = libraryRepository.findAll().get(0);
        Page<Reservation> expected = reservationRepository.findReservationsForALibraryByCriteria(
                foundLibrary.getId(),
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(10),
                List.of(ReservationStatus.PENDING, ReservationStatus.DELAYED, ReservationStatus.IN_PROGRESS),
                PageRequest.of(0, 1));

        Assertions.assertThat(expected).isNotEmpty();
    }

    @Test
    public void givenWrongTimePeriod_findReservationsForALibraryByCriteria_ReturnIsEmpty() {
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        reservationRepository.save(testReservation);
        libraryRepository.save(testLibrary);

        Library foundLibrary = libraryRepository.findAll().get(0);
        Page<Reservation> expected = reservationRepository.findReservationsForALibraryByCriteria(
                foundLibrary.getId(),
                LocalDate.now().minusDays(3),
                LocalDate.now(),
                List.of(ReservationStatus.PENDING, ReservationStatus.DELAYED, ReservationStatus.IN_PROGRESS),
                PageRequest.of(0, 1));

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenWrongStatus_findReservationsForALibraryByCriteria_ReturnIsEmpty() {
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testReservation.setReservedExemplar(testBookExemplar);
        testReservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(testReservation);
        libraryRepository.save(testLibrary);

        Library foundLibrary = libraryRepository.findAll().get(0);
        Page<Reservation> expected = reservationRepository.findReservationsForALibraryByCriteria(
                foundLibrary.getId(),
                LocalDate.now().minusDays(3),
                LocalDate.now().plusDays(10),
                List.of(ReservationStatus.PENDING, ReservationStatus.DELAYED, ReservationStatus.IN_PROGRESS),
                PageRequest.of(0, 1));

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenUserIdAndCriteria_findReservationsForAUserByCriteria_ReturnIsNotEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(1));
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        User foundUser = userRepository.findAll().get(0);
        Page<Reservation> expected = reservationRepository.findReservationsForAUserByCriteria(
                foundUser.getId(),
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(ReservationStatus.PENDING),
                PageRequest.of(0, 1));

        Assertions.assertThat(expected).isNotEmpty();
    }

    @Test
    public void givenWrongUserId_findReservationsForAUserByCriteria_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(1));
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        Page<Reservation> expected = reservationRepository.findReservationsForAUserByCriteria(
                10L,
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(ReservationStatus.PENDING),
                PageRequest.of(0, 1));

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenWrongStatus_findReservationsForAUserByCriteria_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(1));
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        User foundUser = userRepository.findAll().get(0);
        Page<Reservation> expected = reservationRepository.findReservationsForAUserByCriteria(
                foundUser.getId(),
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                List.of(ReservationStatus.IN_PROGRESS),
                PageRequest.of(0, 1));

        Assertions.assertThat(expected).isEmpty();
    }

    @Test
    public void givenWrongTimePeriod_findReservationsForAUserByCriteria_ReturnIsEmpty() {
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        testReservation.setReservedUser(testUser);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(1));
        reservationRepository.save(testReservation);
        userRepository.save(testUser);

        User foundUser = userRepository.findAll().get(0);
        Page<Reservation> expected = reservationRepository.findReservationsForAUserByCriteria(
                        foundUser.getId(),
                        LocalDate.now().minusDays(10),
                        LocalDate.now().plusDays(1),
                        List.of(ReservationStatus.IN_PROGRESS),
                        PageRequest.of(0, 1));

        Assertions.assertThat(expected).isEmpty();
    }
}
