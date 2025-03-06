package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.*;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.ReservationRepository;
import com.project.bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Reservation reserveBook(Long userId, Long bookId, Reservation reservation) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        BookExemplar bookExemplar = bookExemplarRepository.findFirstExemplarAvailable(foundBook.getId(), reservation.getStartDate(), reservation.getEndDate())
                .orElseThrow(() -> new BookExemplarNotAvailableException("Book exemplar cannot be reserved for the given period"));
        if (reservation.getEndDate().isAfter(reservation.getStartDate().plusDays(bookExemplar.getMaximumReservationDuration()))) {
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

    public Reservation updateReservationStatus(Long librarianId, Long reservationId, ReservationStatus reservationStatus) {
        Reservation foundReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation with id %s not found".formatted(reservationStatus)));
        if (!librarianId.equals(foundReservation.getReservedExemplar().getBook().getLibrary().getLibrarian().getId())) {
            throw new UnauthorizedLibrarianAccessException("Librarian doesn't have access to the specified book exemplar");
        }
        if (!foundReservation.getReservationStatus().isNextStatePossible(reservationStatus)) {
            throw new UnavailableStatusChangeException("Cannot change reservation status");
        }
        foundReservation.setReservationStatus(reservationStatus);
        return reservationRepository.save(foundReservation);
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }
}
