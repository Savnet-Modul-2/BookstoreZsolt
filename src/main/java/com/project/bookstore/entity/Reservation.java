package com.project.bookstore.entity;

import com.project.bookstore.entity.types.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "reservation")
@Table(name = "reservation", schema = "public")
public class Reservation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    private User reservedUser;
    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    private BookExemplar reservedExemplar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public User getReservedUser() {
        return reservedUser;
    }

    public void setReservedUser(User reservedUser) {
        this.reservedUser = reservedUser;
    }

    public BookExemplar getReservedExemplar() {
        return reservedExemplar;
    }

    public void setReservedExemplar(BookExemplar reservedExemplar) {
        this.reservedExemplar = reservedExemplar;
    }
}
