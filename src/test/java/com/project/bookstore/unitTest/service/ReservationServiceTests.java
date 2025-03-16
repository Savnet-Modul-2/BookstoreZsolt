package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.*;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.*;
import com.project.bookstore.repository.*;
import com.project.bookstore.service.EmailService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTests {
    @Mock
    private LibraryRepository libraryRepository;
    @Mock
    private EmailService emailService;
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
    private Library testLibrary;
    private Librarian testLibrarian;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testLibrarian = new Librarian();
        testLibrary = new Library();
        testBook = new Book();
        testBookExemplar = new BookExemplar();
        testReservation = new Reservation();
        testUser.setId(1L);
        testLibrarian.setId(1L);
        testLibrarian.setLibrary(testLibrary);
        testLibrary.getBooks().add(testBook);
        testBook.setId(1L);
        testBook.setLibrary(testLibrary);
        testBook.getBookExemplars().add(testBookExemplar);
        testBookExemplar.setId(1L);
        testBookExemplar.setBook(testBook);
        testReservation.setReservedExemplar(testBookExemplar);
    }

    @Test
    public void givenReservation_ReserveBook_ReturnReservation() {
        testUser.setVerifiedAccount(true);
        testBookExemplar.setMaximumReservationDuration(10);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.of(testBookExemplar));

        reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation);
        ArgumentCaptor<Reservation> reservationArgumentCaptor = ArgumentCaptor.forClass(Reservation.class);
        Mockito.verify(reservationRepository).save(reservationArgumentCaptor.capture());
        Reservation capturedReservation = reservationArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(capturedReservation).isEqualTo(testReservation);
    }

    @Test
    public void givenWrongUserId_ReserveBook_ThrowException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id %s not found".formatted(testUser.getId()));
    }

    @Test
    public void givenWrongBookId_ReserveBook_ThrowException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Book with id %s not found".formatted(testBook.getId()));
    }

    @Test
    public void givenBookWithNoExemplars_ReserveBook_ThrowException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(BookExemplarNotAvailableException.class)
                .hasMessageContaining("Book exemplar cannot be reserved for the given period");
    }

    @Test
    public void givenWrongReservationDates_ReserveBook_ThrowException() {
        testBookExemplar.setMaximumReservationDuration(2);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        testUser.setVerifiedAccount(true);
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.of(testBookExemplar));

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(MaximumReservationDurationExceededException.class)
                .hasMessageContaining("The number of days for the exemplar to be reserved has exceeded the reservable duration of days");
    }

    @Test
    public void givenUserNotVerified_ReserveBook_ThrowException() {
        testBookExemplar.setMaximumReservationDuration(10);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setEndDate(LocalDate.now().plusDays(3));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
        Mockito.when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testBook));
        Mockito.when(bookExemplarRepository.findFirstExemplarAvailable(testBook.getId(), testReservation.getStartDate(), testReservation.getEndDate())).thenReturn(Optional.of(testBookExemplar));

        Assertions.assertThatThrownBy(() -> reservationService.reserveBook(testUser.getId(), testBook.getId(), testReservation))
                .isInstanceOf(EntityAccountNotVerifiedException.class)
                .hasMessageContaining("The user's account is not verified to perform this action");
    }

    @Test
    public void givenTimePeriodAndLibraryId_findReservationsForALibraryByCriteria_ReturnContainsReservation() {
        Pageable testPage = PageRequest.of(0, 1);
        List<Reservation> testReservationList = List.of(testReservation);
        Mockito.when(libraryRepository.findById(testLibrary.getId())).thenReturn(Optional.of(testLibrary));
        Mockito.when(reservationService.findReservationsForALibraryByCriteria(testLibrary.getId(), LocalDate.now(), LocalDate.now().plusDays(10), List.of(ReservationStatus.PENDING), testPage)).thenReturn(new PageImpl<>(testReservationList, testPage, testReservationList.size()));

        Page<Reservation> reservationPage = reservationService.findReservationsForALibraryByCriteria(testLibrary.getId(), LocalDate.now(), LocalDate.now().plusDays(10), List.of(ReservationStatus.PENDING), testPage);

        Assertions.assertThat(reservationPage).contains(testReservation);
        Mockito.verify(reservationRepository, Mockito.times(1)).findReservationsForALibraryByCriteria(testLibrary.getId(), LocalDate.now(), LocalDate.now().plusDays(10), List.of(ReservationStatus.PENDING), testPage);
    }

    @Test
    public void givenWrongLibraryId_FindReservationsForALibraryByCriteria_ThrowException() {
        Mockito.when(libraryRepository.findById(testLibrary.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.findReservationsForALibraryByCriteria(testLibrary.getId(), LocalDate.now(), LocalDate.now().plusDays(10), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Library with id %s not found".formatted(testLibrary.getId()));

        Mockito.verify(libraryRepository, Mockito.times(1)).findById(testLibrary.getId());
    }

    @Test
    public void givenWrongTimePeriod_FindReservationsForALibraryByCriteria_ThrowException() {
        Mockito.when(libraryRepository.findById(testLibrary.getId())).thenReturn(Optional.of(testLibrary));

        Assertions.assertThatThrownBy(() -> reservationService.findReservationsForALibraryByCriteria(testLibrary.getId(), LocalDate.now().plusDays(3), LocalDate.now(), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("startDate can't be after endDate");
        Mockito.verify(reservationRepository, Mockito.never()).findReservationsForALibraryByCriteria(testLibrary.getId(), LocalDate.now().plusDays(3), LocalDate.now(), List.of(ReservationStatus.PENDING, ReservationStatus.DELAYED, ReservationStatus.IN_PROGRESS), PageRequest.of(0, 1));
    }

    @Test
    public void givenTimePeriodAndUserId_FindReservationsForAUserByCriteria_ReturnContainsReservation() {
        Pageable testPage = PageRequest.of(0, 1);
        List<Reservation> testReservationList = List.of(testReservation);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
        Mockito.when(reservationRepository.findReservationsForAUserByCriteria(testUser.getId(), LocalDate.now(), LocalDate.now().plusDays(1), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1))).thenReturn(new PageImpl<>(testReservationList, testPage, testReservationList.size()));

        Page<Reservation> reservationPage = reservationService.findReservationsForAUserByCriteria(testUser.getId(), LocalDate.now(), LocalDate.now().plusDays(1), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1));

        Assertions.assertThat(reservationPage).contains(testReservation);
        Mockito.verify(reservationRepository, Mockito.times(1)).findReservationsForAUserByCriteria(testUser.getId(), LocalDate.now(), LocalDate.now().plusDays(1), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1));
    }

    @Test
    public void givenWrongUser_FindReservationsForAUserByCriteria_ThrowException() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.findReservationsForAUserByCriteria(testUser.getId(), LocalDate.now(), LocalDate.now().plusDays(1), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id %s not found".formatted(testUser.getId()));
        Mockito.verify(reservationRepository, Mockito.never()).findReservationsForAUserByCriteria(testUser.getId(), LocalDate.now(), LocalDate.now().plusDays(1), List.of(ReservationStatus.PENDING), PageRequest.of(0, 1));
    }

    @Test
    public void givenNothing_FindAllReservations_VerifyCalledMethod() {
        reservationService.findAllReservations();

        Mockito.verify(reservationRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void givenReservationId_UpdateReservationStatus_ReturnReservation() {
        Long librarianId = 1L;
        ReservationStatus reservationStatus = ReservationStatus.IN_PROGRESS;
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        Mockito.when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));

        reservationService.updateReservationStatus(librarianId, testReservation.getId(), reservationStatus);
        ArgumentCaptor<Reservation> reservationArgumentCaptor = ArgumentCaptor.forClass(Reservation.class);
        Mockito.verify(reservationRepository).save(reservationArgumentCaptor.capture());
        Reservation capturedReservation = reservationArgumentCaptor.getValue();

        Assertions.assertThat(capturedReservation).isEqualTo(testReservation);
    }

    @Test
    public void givenWrongReservationId_UpdateReservationStatus_ThrowException() {
        Long librarianId = 1L;
        Mockito.when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.updateReservationStatus(librarianId, testReservation.getId(), ReservationStatus.IN_PROGRESS))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Reservation with id %s not found".formatted(testReservation.getId()));
    }

    @Test
    public void givenWrongLibrarianId_UpdateReservationStatus_ThrowException() {
        testReservation.getReservedExemplar().getBook().getLibrary().getLibrarian().setId(1L);
        Mockito.when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));

        Assertions.assertThatThrownBy(() -> reservationService.updateReservationStatus(2L, testReservation.getId(), ReservationStatus.IN_PROGRESS))
                .isInstanceOf(UnauthorizedLibrarianAccessException.class)
                .hasMessageContaining("Librarian doesn't have access to the specified book exemplar");
    }

    @Test
    public void givenIncorrectStatus_UpdateReservationStatus_ThrowException() {
        Long librarianId = 1L;
        testReservation.setReservationStatus(ReservationStatus.PENDING);
        Mockito.when(reservationRepository.findById(testReservation.getId())).thenReturn(Optional.of(testReservation));

        Assertions.assertThatThrownBy(() -> reservationService.updateReservationStatus(librarianId, testReservation.getId(), ReservationStatus.FINISHED))
                .isInstanceOf(InvalidStatusChangeException.class)
                .hasMessageContaining("Cannot change reservation status");

    }
}
