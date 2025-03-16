package com.project.bookstore.unitTest.entity;

import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.entity.types.Gender;
import com.project.bookstore.helper.CodeGenerator;
import com.project.bookstore.helper.PasswordEncryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class UserTests {
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
    }

    @Test
    public void givenId_GetId_ReturnNotNull() {
        testUser.setId(1L);

        Assertions.assertThat(testUser.getId()).isNotNull();
    }

    @Test
    public void givenNothing_GetId_ReturnNull() {
        Assertions.assertThat(testUser.getId()).isNull();
    }

    @Test
    public void givenFirstName_GetFirstName_ReturnNotEmpty() {
        testUser.setFirstName("testFirstName");

        Assertions.assertThat(testUser.getFirstName()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetFirstName_ReturnNull() {
        Assertions.assertThat(testUser.getFirstName()).isNull();
    }

    @Test
    public void givenLastName_GetLastName_ReturnNotEmpty() {
        testUser.setLastName("testLastName");

        Assertions.assertThat(testUser.getLastName()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetLastName_ReturnNull() {
        Assertions.assertThat(testUser.getLastName()).isNull();
    }

    @Test
    public void givenEmail_GetEmail_ReturnNotEmpty() {
        testUser.setEmail("testEmail@gmail.com");

        Assertions.assertThat(testUser.getEmail()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetEmail_ReturnNull() {
        Assertions.assertThat(testUser.getEmail()).isNull();
    }

    @Test
    public void givePhoneNumber_GetPhoneNumber_ReturnNotEmpty() {
        testUser.setPhoneNumber("123456789");

        Assertions.assertThat(testUser.getPhoneNumber()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetPhoneNumber_ReturnNull() {
        Assertions.assertThat(testUser.getPhoneNumber()).isNull();
    }

    @Test
    public void givenPassword_GetPassword_ReturnNotEmpty() {
        testUser.setPassword(PasswordEncryptor.encryptPasswordWithSHA256("testPassword"));

        Assertions.assertThat(testUser.getPassword()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetPassword_ReturnNull() {
        Assertions.assertThat(testUser.getPassword()).isNull();
    }

    @Test
    public void givenCountry_GetCountry_ReturnNotEmpty() {
        testUser.setCountry("testCountry");

        Assertions.assertThat(testUser.getCountry()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetCountry_ReturnNull() {
        Assertions.assertThat(testUser.getCountry()).isNull();
    }

    @Test
    public void givenGender_GetGender_ReturnNotNull() {
        testUser.setGender(Gender.MALE);

        Assertions.assertThat(testUser.getGender()).isNotNull();
    }

    @Test
    public void givenNothing_GetGender_ReturnNull() {
        Assertions.assertThat(testUser.getGender()).isNull();
    }

    @Test
    public void givenYearOfBirth_GetYearOfBirth_ReturnGreaterThan() {
        testUser.setYearOfBirth(2000);

        Assertions.assertThat(testUser.getYearOfBirth()).isGreaterThan(0);
    }

    @Test
    public void givenNothing_GetYearOfBirth_ReturnIsZero() {
        Assertions.assertThat(testUser.getYearOfBirth()).isZero();
    }

    @Test
    public void givenVerifiedAccount_IsVerifiedAccount_ReturnTrue() {
        testUser.setVerifiedAccount(true);

        Assertions.assertThat(testUser.isVerifiedAccount()).isTrue();
    }

    @Test
    public void givenNothing_IsVerifiedAccount_ReturnFalse() {
        Assertions.assertThat(testUser.isVerifiedAccount()).isFalse();
    }

    @Test
    public void givenVerificationCode_GetVerificationCode_ReturnNotEmpty() {
        testUser.setVerificationCode(CodeGenerator.generateCode());

        Assertions.assertThat(testUser.getVerificationCode()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetVerificationCode_ReturnNull() {
        Assertions.assertThat(testUser.getVerificationCode()).isNull();
    }

    @Test
    public void givenVerificationCodeTime_GetVerificationCodeTime_ReturnNotNull() {
        testUser.setVerificationCodeTime(LocalDateTime.now());

        Assertions.assertThat(testUser.getVerificationCodeTime()).isNotNull();
    }

    @Test
    public void givenNothing_GetVerificationCodeTime_ReturnNull() {
        Assertions.assertThat(testUser.getVerificationCodeTime()).isNull();
    }

    @Test
    public void givenReservationList_GetReservations_ReturnNotEmpty() {
        testUser.setReservations(List.of(new Reservation()));

        Assertions.assertThat(testUser.getReservations()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetReservations_ReturnIsEmpty() {
        Assertions.assertThat(testUser.getReservations()).isEmpty();
    }
}
