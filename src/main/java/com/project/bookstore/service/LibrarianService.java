package com.project.bookstore.service;

import com.project.bookstore.entity.Librarian;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.repository.LibrarianRepository;
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

    public Librarian createLibrarian(Librarian librarian) {
        if (librarianRepository.findByEmail(librarian.getEmail()).isPresent()) {
            throw new EntityExistsException("Librarian with the email address %s already exists".formatted(librarian.getEmail()));
        }
        String librarianVerificationCode = CodeGenerator.generateCode();
        librarian.setVerificationCode(CodeGenerator.generateCode());
        librarian.setVerificationCodeTime(LocalDateTime.now());
        emailService.sendEmail(new EmailDetails(librarian.getEmail(), EmailDetails.CODE_EMAIL_SUBJECT, EmailDetails.CODE_EMAIL_STRING.formatted(librarianVerificationCode)));
        return librarianRepository.save(librarian);
    }

    public Librarian findLibrarianById(Long id) {
        return librarianRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Librarian> findAllLibrarians() {
        return librarianRepository.findAll();
    }

    public void deleteLibrarianById(Long id) {
        librarianRepository.deleteById(id);
    }

    public Librarian verifyLibrarian(Long id, String verificationCode) {
        Librarian librarian = librarianRepository.findById(id).orElseThrow(EntityNotFoundException::new);
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

}

