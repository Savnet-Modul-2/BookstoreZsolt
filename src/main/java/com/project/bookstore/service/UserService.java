package com.project.bookstore.service;

import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.exceptions.EntityAccountNotVerifiedException;
import com.project.bookstore.exceptions.EntityBadCredentialsException;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.helper.PasswordEncryptor;
import com.project.bookstore.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("User with the email address %s already exists".formatted(user.getEmail()));
        }
        String userVerificationCode = CodeGenerator.generateCode();
        user.setVerificationCode(userVerificationCode);
        user.setVerificationCodeTime(LocalDateTime.now());
        emailService.sendEmail(new EmailDetails(user.getEmail(), EmailDetails.CODE_EMAIL_SUBJECT, EmailDetails.CODE_EMAIL_BODY.formatted(userVerificationCode)));
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
    }

    public User verifyUserCode(Long id, String code) {
        User user = userRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Duration duration = Duration.between(user.getVerificationCodeTime(), LocalDateTime.now());
        if (user.getVerificationCode().equals(code) && duration.toMinutes() < 60) {
            user.setVerifiedAccount(true);
            user.setVerificationCodeTime(null);
            user.setVerificationCode(null);
        } else {
            throw new CodeExpirationTimeException("The time for code verification has expired");
        }
        return userRepository.save(user);
    }

    public Long getUserIdAfterLogin(String userEmail, String userPassword) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email %s not found".formatted(userEmail)));
        if (!user.isVerifiedAccount()) {
            throw new EntityAccountNotVerifiedException("This account is not yet verified");
        }
        if (!user.getPassword().equals(PasswordEncryptor.encryptUserPasswordWithSHA256(userPassword))) {
            throw new EntityBadCredentialsException("Couldn't login to the account with the provided password");
        }
        return user.getId();
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public void sendDelayedReservationEmail(List<Reservation> reservationList) {
        reservationList.forEach(reservation -> emailService.sendEmail(new EmailDetails(
                reservation.getReservedUser().getEmail(),
                EmailDetails.RESERVATION_DELAYED_EMAIL_SUBJECT,
                EmailDetails.RESERVATION_DELAYED_EMAIL_BODY.formatted(
                        reservation.getReservedExemplar().getBook().getTitle(),
                        reservation.getReservedExemplar().getBook().getLibrary().getName(),
                        reservation.getReservedExemplar().getBook().getLibrary().getCity())
        )));
    }
}
