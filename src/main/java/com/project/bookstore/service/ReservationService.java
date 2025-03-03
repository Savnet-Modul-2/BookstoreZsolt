package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.BookExemplarNotAvailableException;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.ReservationRepository;
import com.project.bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Reservation reserveBook(Long userId, Long bookId, Reservation reservation) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        BookExemplar bookExemplar = bookExemplarRepository.findFirstExemplarAvailable(foundBook.getId(), reservation.getStartDate(), reservation.getEndDate())
                .orElseThrow(() -> new BookExemplarNotAvailableException("Book exemplar cannot be reserved for the given period"));
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setReservedUser(foundUser);
        reservation.setReservedExemplar(bookExemplar);
        return reservationRepository.save(reservation);
    }
}
