package com.project.bookstore.service;

import com.project.bookstore.entity.Librarian;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.*;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.helper.PasswordEncryptor;
import com.project.bookstore.repository.LibrarianRepository;
import com.project.bookstore.repository.ReservationRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibrarianService {
    @Autowired
    private LibrarianRepository librarianRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ReservationRepository reservationRepository;

    public Librarian createLibrarian(Librarian librarian) {
        if (librarianRepository.existsByEmail(librarian.getEmail())) {
            throw new EntityExistsException("Librarian with the email address %s already exists".formatted(librarian.getEmail()));
        }
        String librarianVerificationCode = CodeGenerator.generateCode();
        librarian.setVerificationCode(librarianVerificationCode);
        librarian.setVerificationCodeTime(LocalDateTime.now());
        emailService.sendEmail(new EmailDetails(librarian.getEmail(), EmailDetails.CODE_EMAIL_SUBJECT, EmailDetails.CODE_EMAIL_STRING.formatted(librarianVerificationCode)));
        return librarianRepository.save(librarian);
    }

    public Librarian findById(Long id) {
        return librarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Librarian with id %s not found".formatted(id)));
    }

    public List<Librarian> findAll() {
        return librarianRepository.findAll();
    }

    public Librarian verifyLibrarian(Long id, String verificationCode) {
        Librarian librarian = librarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Librarian with id %s not found".formatted(id)));
        Duration duration = Duration.between(librarian.getVerificationCodeTime(), LocalDateTime.now());
        if (librarian.getVerificationCode().equals(verificationCode) && duration.toMinutes() < 60) {
            librarian.setVerifiedAccount(true);
            librarian.setVerificationCodeTime(null);
            librarian.setVerificationCode(null);
        } else {
            throw new CodeExpirationTimeException("The time for code verification has expired");
        }
        return librarianRepository.save(librarian);
    }

    public Long getLibrarianIdAfterLogin(String librarianEmail, String librarianPassword) {
        Librarian librarian = librarianRepository.findByEmail(librarianEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email %s not found".formatted(librarianEmail)));
        if (!librarian.isVerifiedAccount()) {
            throw new EntityAccountNotVerifiedException("This account is not yet verified");
        }
        if (!librarian.getPassword().equals(PasswordEncryptor.encryptUserPasswordWithSHA256(librarianPassword))) {
            throw new EntityBadCredentialsException("Couldn't login to the account with the provided password");
        }
        return librarian.getId();
    }

    public void deleteById(Long id) {
        librarianRepository.deleteById(id);
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
}

