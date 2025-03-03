package com.project.bookstore.unitTest.service;

import com.project.bookstore.entity.Librarian;
import com.project.bookstore.exceptions.CodeExpirationTimeException;
import com.project.bookstore.exceptions.EntityAccountNotVerifiedException;
import com.project.bookstore.exceptions.EntityBadCredentialsException;
import com.project.bookstore.repository.LibrarianRepository;
import com.project.bookstore.service.EmailService;
import com.project.bookstore.service.LibrarianService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        testLibrarian.setPassword("testPassword");
        testLibrarian.setEmail("testEmail@gmail.com");
    }

    @Test
    public void testLibrarianIsCreated() {
        librarianService.createLibrarian(testLibrarian);

        ArgumentCaptor<Librarian> librarianArgumentCaptor = ArgumentCaptor.forClass(Librarian.class);
        Mockito.verify(librarianRepository).save(librarianArgumentCaptor.capture());
        Librarian capturedLibrarian = librarianArgumentCaptor.getValue();

        AssertionsForClassTypes.assertThat(capturedLibrarian).isEqualTo(testLibrarian);
    }

    @Test
    public void testLibrarianEmilExistsThrowsException() {
        Mockito.when(librarianRepository.existsByEmail(testLibrarian.getEmail())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> librarianService.createLibrarian(testLibrarian))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("Librarian with the email address %s already exists".formatted(testLibrarian.getEmail()));
        Mockito.verify(librarianRepository, Mockito.never()).save(testLibrarian);
    }

    @Test
    public void testFindById() {
        Mockito.when(librarianRepository.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));

        Librarian foundLibrarian = librarianService.findById(testLibrarian.getId());
        AssertionsForClassTypes.assertThat(foundLibrarian).isEqualTo(testLibrarian);

        Mockito.verify(librarianRepository, Mockito.times(1)).findById(testLibrarian.getId());
    }

    @Test
    public void testFindByIdThrowsException() {
        Mockito.when(librarianRepository.findById(testLibrarian.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> librarianService.findById(testLibrarian.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        Mockito.verify(librarianRepository, Mockito.times(1)).findById(testLibrarian.getId());
    }

    @Test
    public void testFindAll() {
        librarianService.findAll();

        Mockito.verify(librarianRepository).findAll();
    }

    @Test
    public void testDeleteById() {
        librarianService.deleteById(testLibrarian.getId());

        Mockito.verify(librarianRepository).deleteById(testLibrarian.getId());
    }

    @Test
    public void testVerifyLibrarianCodeWithLibrarianFound() {
        Mockito.when(librarianRepository.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));

        librarianService.verifyLibrarian(testLibrarian.getId(), testLibrarian.getVerificationCode());

        Assertions.assertThat(testLibrarian.getVerificationCode()).isEqualTo(null);
        //TODO: isEqualTo expect a LDT String, should convert somehow to null
        //Assertions.assertThat(testUser.getVerificationCodeTime());
        Assertions.assertThat(testLibrarian.isVerifiedAccount()).isEqualTo(true);
    }

    @Test
    public void testVerifyLibrarianCodeNotEqualThrowException() {
        String errorVerificationCode = "errorVerificationCode";
        Mockito.when(librarianRepository.findById(testLibrarian.getId())).thenReturn(Optional.of(testLibrarian));

        Assertions.assertThatThrownBy(() -> librarianService.verifyLibrarian(testLibrarian.getId(), errorVerificationCode))
                .isInstanceOf(CodeExpirationTimeException.class)
                .hasMessageContaining("The time for code verification has expired");
    }

    //TODO: Check to make this test run
    @Test
    @Disabled
    public void testGetLibrarianIdAfterLogin() {
        Mockito.when(librarianRepository.findByEmail(testLibrarian.getEmail())).thenReturn(Optional.of(testLibrarian));
        testLibrarian.setVerifiedAccount(true);
        Long testId = librarianService.getLibrarianIdAfterLogin(testLibrarian.getEmail(), testLibrarian.getPassword());
        Assertions.assertThat(testId).isEqualTo(testLibrarian.getId());
    }

    @Test
    public void testGetUserIdAfterLoginWhenNotVerifiedThrowException() {
        Mockito.when(librarianRepository.findByEmail(testLibrarian.getEmail())).thenReturn(Optional.of(testLibrarian));
        Assertions.assertThat(testLibrarian.isVerifiedAccount()).isFalse();
        Assertions.assertThatThrownBy(() -> librarianService.getLibrarianIdAfterLogin(testLibrarian.getEmail(), testLibrarian.getPassword()))
                .isInstanceOf(EntityAccountNotVerifiedException.class)
                .hasMessageContaining("This account is not yet verified");
    }

    @Test
    public void testGetUserIdAfterLoginWhenBadCredentialsException() {
        String errorPassword = "errorPassword";
        Mockito.when(librarianRepository.findByEmail(testLibrarian.getEmail())).thenReturn(Optional.of(testLibrarian));
        testLibrarian.setVerifiedAccount(true);
        Assertions.assertThatThrownBy(() -> librarianService.getLibrarianIdAfterLogin(testLibrarian.getEmail(), errorPassword))
                .isInstanceOf(EntityBadCredentialsException.class)
                .hasMessageContaining("Couldn't login to the account with the provided password");
    }
}
