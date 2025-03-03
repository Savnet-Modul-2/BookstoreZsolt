package com.project.bookstore.dto;

import com.project.bookstore.entity.types.ReservationStatus;

import java.time.LocalDate;

public class ReservationDto {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus reservationStatus;
    private UserDto user;
    private BookExemplarDto bookExemplar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public BookExemplarDto getBookExemplar() {
        return bookExemplar;
    }

    public void setBookExemplar(BookExemplarDto bookExemplar) {
        this.bookExemplar = bookExemplar;
    }
}
