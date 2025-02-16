package com.project.bookstore.service;

import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.EmailDetails;
import com.project.bookstore.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private EmailService emailService;


    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EntityExistsException("User with the email address %s already exists".formatted(user.getEmail()));
        }
        String userVerificationCode = CodeGenerator.generateCode();
        user.setVerificationCode(userVerificationCode);
        user.setVerificationCodeTime(LocalDateTime.now());
        emailService.sendVerificationCodeMail(new EmailDetails(user.getEmail(), EmailDetails.CODE_EMAIL_SUBJECT, CodeGenerator.CODE_EMAIL_STRING.formatted(userVerificationCode)));
        return userRepository.save(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User verifyUserCode(Long id, String code) throws CodeExpirationTimeException {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (user.getVerificationCode().equals(code) && user.getVerificationCodeTime().isBefore(LocalDateTime.now().plusHours(1))) {
            user.setVerifiedAccount(true);
            user.setVerificationCodeTime(null);
            user.setVerificationCode(null);
        }else{
            throw new CodeExpirationTimeException("The time for code verification has expired");
        }
        return userRepository.save(user);
    }

}
