package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.exceptions.EntityAccountNotVerifiedException;
import com.project.bookstore.exceptions.EntityBadCredentialsException;
import com.project.bookstore.repository.UserRepository;
import com.project.bookstore.service.EmailService;
import com.project.bookstore.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private UserService userService;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("testFirstName");
        testUser.setLastName("testLastName");
        testUser.setEmail("testEmail@gmail.com");
        testUser.setPassword("testPassword");
        testUser.setVerificationCode("testVerificationCode");
        testUser.setVerificationCodeTime(LocalDateTime.now());
    }

    @Test
    public void testUserIsCreated() {
        userService.createUser(testUser);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(capturedUser).isEqualTo(testUser);
    }

    @Test
    public void testUserEmailExistsThrowsException() {
        Mockito.when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> userService.createUser(testUser))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("User with the email address %s already exists".formatted(testUser.getEmail()));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testFindAll() {
        userService.findAll();

        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void testFindById() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User foundUser = userService.findById(testUser.getId());

        AssertionsForClassTypes.assertThat(foundUser).isEqualTo(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).findById(testUser.getId());
    }

    @Test
    public void testFindByIdThrowsException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.findById(testUser.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id %s not found".formatted(testUser.getId()));
    }

    @Test
    public void testDeleteById() {
        userService.deleteById(testUser.getId());

        Mockito.verify(userRepository).deleteById(testUser.getId());
    }

    //TODO: should assert that service method return same User from test
    @Test
    public void testVerifyUserCodeWithUserFound() {
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        userService.verifyUserCode(testUser.getId(), testUser.getVerificationCode());

        Assertions.assertThat(testUser.getVerificationCode()).isEqualTo(null);

        //TODO: isEqualTo expect a LDT String, should convert somehow to null
        //Assertions.assertThat(testUser.getVerificationCodeTime());
        Assertions.assertThat(testUser.isVerifiedAccount()).isEqualTo(true);
    }

    @Test
    public void testVerifyUserCodeNotEqualThrowException() {
        String errorVerificationCode = "errorVerificationCode";
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        Assertions.assertThatThrownBy(() -> userService.verifyUserCode(testUser.getId(), errorVerificationCode))
                .isInstanceOf(CodeExpirationTimeException.class)
                .hasMessageContaining("The time for code verification has expired");
    }

    //TODO: Check to make this test run
    @Test
    @Disabled
    public void testGetUserIdAfterLogin(){
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        testUser.setVerifiedAccount(true);

        Long testId = userService.getUserIdAfterLogin(testUser.getEmail(), testUser.getPassword());

        Assertions.assertThat(testId).isEqualTo(testUser.getId());
    }

    @Test
    public void testGetUserIdAfterLoginWhenNotVerifiedThrowException() {
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        Assertions.assertThat(testUser.isVerifiedAccount()).isFalse();
        Assertions.assertThatThrownBy(() -> userService.getUserIdAfterLogin(testUser.getEmail(), testUser.getPassword()))
                .isInstanceOf(EntityAccountNotVerifiedException.class)
                .hasMessageContaining("This account is not yet verified");
    }

    @Test
    public void testGetUserIdAfterLoginWhenBadCredentialsException() {
        String errorPassword = "errorPassword";
        Mockito.when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        testUser.setVerifiedAccount(true);

        Assertions.assertThatThrownBy(() -> userService.getUserIdAfterLogin(testUser.getEmail(), errorPassword))
                .isInstanceOf(EntityBadCredentialsException.class)
                .hasMessageContaining("Couldn't login to the account with the provided password");
    }
}
