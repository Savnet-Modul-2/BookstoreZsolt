package com.project.bookstore.unitTest.entity;

import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.User;
import com.project.bookstore.entity.types.ReservationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class ReservationTests {
    private Reservation testReservation;

    @BeforeEach
    public void setUp() {
        testReservation = new Reservation();
    }

    @Test
    public void givenId_GetId_ReturnNotNull() {
        testReservation.setId(1L);

        Assertions.assertThat(testReservation.getId()).isNotNull();
    }

    @Test
    public void givenNothing_GetId_ReturnNull() {
        Assertions.assertThat(testReservation.getId()).isNull();
    }

    @Test
    public void givenStartDate_GetStartDate_ReturnNotNull() {
        testReservation.setStartDate(LocalDate.now());

        Assertions.assertThat(testReservation.getStartDate()).isNotNull();
    }

    @Test
    public void givenNothing_GetStartDate_ReturnNull() {
        Assertions.assertThat(testReservation.getStartDate()).isNull();
    }

    @Test
    public void givenEndDate_GetEndDate_ReturnNotNull() {
        testReservation.setEndDate(LocalDate.now());

        Assertions.assertThat(testReservation.getEndDate()).isNotNull();
    }

    @Test
    public void givenNothing_GetEndDate_ReturnNull() {
        Assertions.assertThat(testReservation.getEndDate()).isNull();
    }

    @Test
    public void givenReservationStatus_GetReservationStatus_ReturnNotNull() {
        testReservation.setReservationStatus(ReservationStatus.IN_PROGRESS);

        Assertions.assertThat(testReservation.getReservationStatus()).isNotNull();
    }

    @Test
    public void givenNothing_GetReservationStatus_ReturnNull() {
        Assertions.assertThat(testReservation.getReservationStatus()).isNull();
    }

    @Test
    public void givenUser_GetReservedUser_ReturnNotNull() {
        testReservation.setReservedUser(new User());

        Assertions.assertThat(testReservation.getReservedUser()).isNotNull();
    }

    @Test
    public void givenNothing_GetReservedUser_ReturnNull() {
        Assertions.assertThat(testReservation.getReservedUser()).isNull();
    }

    @Test
    public void givenBookExemplar_GetReservedExemplar_ReturnNotNull() {
        testReservation.setReservedExemplar(new BookExemplar());

        Assertions.assertThat(testReservation.getReservedExemplar()).isNotNull();
    }

    @Test
    public void givenNothing_GetReservedExemplar_ReturnNull() {
        Assertions.assertThat(testReservation.getReservedExemplar()).isNull();
    }
}
