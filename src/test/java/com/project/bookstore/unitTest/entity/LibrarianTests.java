package com.project.bookstore.unitTest.entity;

import com.project.bookstore.entity.Librarian;
import com.project.bookstore.entity.Library;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.PasswordEncryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class LibrarianTests {
    private Librarian testLibrarian;

    @BeforeEach
    public void setUp() {
        testLibrarian = new Librarian();
    }

    @Test
    public void givenId_GetId_ReturnNotNull() {
        testLibrarian.setId(1L);

        Assertions.assertThat(testLibrarian.getId()).isNotNull();
    }

    @Test
    public void givenNothing_GetId_ReturnNull() {
        Assertions.assertThat(testLibrarian.getId()).isNull();
    }

    @Test
    public void givenName_GetName_ReturnNotEmpty() {
        testLibrarian.setName("testName");

        Assertions.assertThat(testLibrarian.getName()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetName_ReturnNull() {
        Assertions.assertThat(testLibrarian.getName()).isNull();
    }

    @Test
    public void givenEmail_GetEmail_ReturnNotEmpty() {
        testLibrarian.setEmail("testEmail@gmail.com");

        Assertions.assertThat(testLibrarian.getEmail()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetEmail_ReturnNull() {
        Assertions.assertThat(testLibrarian.getEmail()).isNull();
    }

    @Test
    public void givenPassword_GetPassword_ReturnNotEmpty() {
        testLibrarian.setPassword(PasswordEncryptor.encryptPasswordWithSHA256("testPassword"));

        Assertions.assertThat(testLibrarian.getPassword()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetPassword_ReturnNull() {
        Assertions.assertThat(testLibrarian.getPassword()).isNull();
    }

    @Test
    public void givenLibrary_GetLibrary_ReturnNotNull() {
        testLibrarian.setLibrary(new Library());

        Assertions.assertThat(testLibrarian.getLibrary()).isNotNull();
    }

    @Test
    public void givenNothing_GetLibrary_ReturnNull() {
        Assertions.assertThat(testLibrarian.getLibrary()).isNull();
    }

    @Test
    public void givenVerifiedAccount_IsVerifiedAccount_ReturnTrue() {
        testLibrarian.setVerifiedAccount(true);

        Assertions.assertThat(testLibrarian.isVerifiedAccount()).isTrue();
    }

    @Test
    public void givenNothing_IsVerifiedAccount_ReturnFalse() {
        Assertions.assertThat(testLibrarian.isVerifiedAccount()).isFalse();
    }

    @Test
    public void givenVerificationCode_GetVerificationCode_ReturnNotEmpty() {
        testLibrarian.setVerificationCode(CodeGenerator.generateCode());

        Assertions.assertThat(testLibrarian.getVerificationCode()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetVerificationCode_ReturnNull() {
        Assertions.assertThat(testLibrarian.getVerificationCode()).isNull();
    }

    @Test
    public void givenVerificationCodeTime_GetVerificationCodeTime_ReturnNotNull() {
        testLibrarian.setVerificationCodeTime(LocalDateTime.now());

        Assertions.assertThat(testLibrarian.getVerificationCodeTime()).isNotNull();
    }

    @Test
    public void givenNothing_GetVerificationCodeTime_ReturnNull() {
        Assertions.assertThat(testLibrarian.getVerificationCodeTime()).isNull();
    }
}
