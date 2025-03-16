package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.*;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.exceptions.EntityAccountNotVerifiedException;
import com.project.bookstore.exceptions.EntityBadCredentialsException;
import com.project.bookstore.helper.PasswordEncryptor;
import com.project.bookstore.repository.LibrarianRepository;
import com.project.bookstore.service.EmailService;
import com.project.bookstore.service.LibrarianService;
import jakarta.persistence.EntityExistsException;
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

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LibrarianServiceTests {
    @Mock
    private LibrarianRepository librarianRepository;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private LibrarianService librarianService;
    private Librarian testLibrarian;

    @BeforeEach
    public void setUp() {
        testLibrarian = new Librarian();
        testLibrarian.setId(1L);
        testLibrarian.setVerificationCode("testVerificationCode");
        testLibrarian.setVerificationCodeTime(LocalDateTime.now());
        testLibrarian.setPassword(PasswordEncryptor.encryptPasswordWithSHA256("testPassword"));
        testLibrarian.setEmail("testEmail@gmail.com");
    }

    @Test
    public void givenLibrarian_CreateLibrarian_ReturnLibrarian() {
        Mockito.when(librarianRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        librarianService.createLibrarian(testLibrarian);

        ArgumentCaptor<Librarian> librarianArgumentCaptor = ArgumentCaptor.forClass(Librarian.class);
        Mockito.verify(librarianRepository).save(librarianArgumentCaptor.capture());
        Librarian capturedLibrarian = librarianArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(capturedLibrarian).isEqualTo(testLibrarian);
    }

    @Test
    public void givenEmail_ExistsByEmail_ThrowException() {
        Mockito.when(librarianRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> librarianService.createLibrarian(testLibrarian))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("Librarian with the email address %s already exists".formatted(testLibrarian.getEmail()));
        Mockito.verify(librarianRepository, Mockito.never()).save(testLibrarian);
    }

    @Test
    public void givenLibrarianId_FindById_ReturnLibrarian() {
        Mockito.when(librarianRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testLibrarian));

        Librarian foundLibrarian = librarianService.findById(testLibrarian.getId());
        AssertionsForClassTypes.assertThat(foundLibrarian).isEqualTo(testLibrarian);

        Mockito.verify(librarianRepository, Mockito.times(1)).findById(testLibrarian.getId());
    }

    @Test
    public void givenWrongId_FindById_ThrowException() {
        Mockito.when(librarianRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> librarianService.findById(testLibrarian.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(librarianRepository, Mockito.times(1)).findById(testLibrarian.getId());
    }

    @Test
    public void givenNothing_FindAll_VerifyCalledMethod() {
        librarianService.findAll();

        Mockito.verify(librarianRepository).findAll();
    }

    @Test
    public void givenLibrarianId_DeleteById_VerifyCalledMethod() {
        librarianService.deleteById(testLibrarian.getId());

        Mockito.verify(librarianRepository).deleteById(testLibrarian.getId());
    }

    @Test
    public void givenLibrarianIdAndCode_VerifyLibrarian_ReturnVerifiedAccount() {
        Mockito.when(librarianRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testLibrarian));

        librarianService.verifyLibrarian(testLibrarian.getId(), testLibrarian.getVerificationCode());

        Assertions.assertThat(testLibrarian.getVerificationCode()).isEqualTo(null);
        Assertions.assertThat(testLibrarian.getVerificationCodeTime()).isNull();
        Assertions.assertThat(testLibrarian.isVerifiedAccount()).isEqualTo(true);
    }

    @Test
    public void givenWrongCode_VerifyLibrarian_ThrowException() {
        String errorVerificationCode = "errorVerificationCode";
        Mockito.when(librarianRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testLibrarian));

        Assertions.assertThatThrownBy(() -> librarianService.verifyLibrarian(testLibrarian.getId(), errorVerificationCode))
                .isInstanceOf(CodeExpirationTimeException.class)
                .hasMessageContaining("The time for code verification has expired");
    }

    @Test
    public void givenEmailAndPassword_GetLibrarianIdAfterLogin_ReturnLibrarianId() {
        String testPassword = "testPassword";
        Mockito.when(librarianRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(testLibrarian));
        testLibrarian.setVerifiedAccount(true);

        Long testId = librarianService.getLibrarianIdAfterLogin(testLibrarian.getEmail(), testPassword);

        Assertions.assertThat(testId).isEqualTo(testLibrarian.getId());
    }

    @Test
    public void givenNotVerifiedAccount_GetLibrarianIdAfterLogin_ThrowException() {
        Mockito.when(librarianRepository.findByEmail(testLibrarian.getEmail())).thenReturn(Optional.of(testLibrarian));

        Assertions.assertThat(testLibrarian.isVerifiedAccount()).isFalse();
        Assertions.assertThatThrownBy(() -> librarianService.getLibrarianIdAfterLogin(testLibrarian.getEmail(), testLibrarian.getPassword()))
                .isInstanceOf(EntityAccountNotVerifiedException.class)
                .hasMessageContaining("This account is not yet verified");
    }

    @Test
    public void givenWrongPassword_GetLibrarianIdAfterLogin_ThrowException() {
        String errorPassword = "errorPassword";
        Mockito.when(librarianRepository.findByEmail(testLibrarian.getEmail())).thenReturn(Optional.of(testLibrarian));
        testLibrarian.setVerifiedAccount(true);
        Assertions.assertThatThrownBy(() -> librarianService.getLibrarianIdAfterLogin(testLibrarian.getEmail(), errorPassword))
                .isInstanceOf(EntityBadCredentialsException.class)
                .hasMessageContaining("Couldn't login to the account with the provided password");
    }
}
