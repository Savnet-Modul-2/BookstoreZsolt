package com.project.bookstore.service;

import com.project.bookstore.entity.*;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.*;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookExemplarRepository bookExemplarRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private LibraryRepository libraryRepository;

    public Reservation reserveBook(Long userId, Long bookId, Reservation reservation) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        BookExemplar bookExemplar = bookExemplarRepository.findFirstExemplarAvailable(foundBook.getId(), reservation.getStartDate(), reservation.getEndDate())
                .orElseThrow(() -> new BookExemplarNotAvailableException("Book exemplar cannot be reserved for the given period"));
        if (reservation.getEndDate().isBefore(reservation.getStartDate().plusDays(bookExemplar.getMaximumReservationDuration()))) {
            throw new MaximumReservationDurationExceededException("The number of days for the exemplar to be reserved has exceeded the reservable duration of days");
        }
        if (!foundUser.isVerifiedAccount()) {
            throw new EntityAccountNotVerifiedException("The user's account is not verified to perform this action");
        }
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setReservedUser(foundUser);
        reservation.setReservedExemplar(bookExemplar);
        emailService.sendEmail(new EmailDetails(foundUser.getEmail(), EmailDetails.RESERVATION_CONFIRMATION_EMAIL_SUBJECT, EmailDetails.RESERVATION_CONFIRMATION_EMAIL_BODY.formatted(foundBook.getTitle(), foundBook.getLibrary().getName(), foundBook.getLibrary().getCity(), reservation.getStartDate())));
        return reservationRepository.save(reservation);
    }

    public Page<Reservation> findReservationsForALibraryByTimePeriod(Long libraryId, LocalDate startDate, LocalDate endDate, List<ReservationStatus> reservationStatusList, Pageable pageable) {
        Library foundLibrary = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id %s not found".formatted(libraryId)));
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate can't be after endDate");
        }
        return reservationRepository.searchReservationsForALibraryByTimePeriod(foundLibrary.getId(), startDate, endDate, reservationStatusList, pageable);
    }

    public Page<Reservation> findReservationsForAUserByStatus(Long userId, List<ReservationStatus>reservationStatusList, Pageable pageable) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
        return reservationRepository.searchReservationsForAUserByReservationStatus(foundUser.getId(), reservationStatusList, pageable);
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation updateReservationStatus(Long librarianId, Long reservationId, ReservationStatus reservationStatus) {
        Reservation foundReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation with id %s not found".formatted(reservationId)));
        if (!librarianId.equals(foundReservation.getReservedExemplar().getBook().getLibrary().getLibrarian().getId())) {
            throw new UnauthorizedLibrarianAccessException("Librarian doesn't have access to the specified book exemplar");
        }
        if (!foundReservation.getReservationStatus().isNextStatePossible(reservationStatus)) {
            throw new InvalidStatusChangeException("Cannot change reservation status");
        }
        foundReservation.setReservationStatus(reservationStatus);
        return reservationRepository.save(foundReservation);
    }
}
