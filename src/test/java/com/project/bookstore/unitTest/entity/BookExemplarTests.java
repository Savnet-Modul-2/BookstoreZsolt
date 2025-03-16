package com.project.bookstore.unitTest.entity;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BookExemplarTests {
    private BookExemplar testBookExemplar;

    @BeforeEach
    public void setUp() {
        testBookExemplar = new BookExemplar();
    }

    @Test
    public void givenId_GetId_ReturnNotNull() {
        testBookExemplar.setId(1L);

        Assertions.assertThat(testBookExemplar.getId()).isNotNull();
    }

    @Test
    public void givenNothing_GetId_ReturnNull() {
        Assertions.assertThat(testBookExemplar.getId()).isNull();
    }

    @Test
    public void givenPublisher_GetPublisher_ReturnNotEmpty() {
        testBookExemplar.setPublisher("testPublisher");

        Assertions.assertThat(testBookExemplar.getPublisher()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetPublisher_ReturnNull() {
        Assertions.assertThat(testBookExemplar.getPublisher()).isNull();
    }

    @Test
    public void givenMaximumReservationDuration_GetMaximumReservationDuration_ReturnGreaterThan() {
        testBookExemplar.setMaximumReservationDuration(3);

        Assertions.assertThat(testBookExemplar.getMaximumReservationDuration()).isGreaterThan(0);
    }

    @Test
    public void givenNothing_GetMaximumReservationDuration_ReturnIsZero() {
        Assertions.assertThat(testBookExemplar.getMaximumReservationDuration()).isZero();
    }

    @Test
    public void givenBook_GetBook_ReturnNotNull() {
        testBookExemplar.setBook(new Book());

        Assertions.assertThat(testBookExemplar.getBook()).isNotNull();
    }

    @Test
    public void givenNothing_GetBook_ReturnNull() {
        Assertions.assertThat(testBookExemplar.getBook()).isNull();
    }

    @Test
    public void givenReservationList_GetReservations_ReturnNotEmpty() {
        testBookExemplar.setReservations(List.of(new Reservation()));

        Assertions.assertThat(testBookExemplar.getReservations()).isNotEmpty();
    }

    @Test
    public void givenNothing_GetReservations_ReturnIsEmpty() {
        Assertions.assertThat(testBookExemplar.getReservations()).isEmpty();
    }
}
