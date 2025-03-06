package com.project.bookstore.repository;

import com.project.bookstore.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservationStatus = 'PENDING'
            AND reservation.startDate < :now
            """)
    List<Reservation> searchExpiredPendingReservations(LocalDate now);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservationStatus = 'IN_PROGRESS'
            AND  reservation.endDate < :now
            """)
    List<Reservation> searchExpiredDelayedReservations(LocalDate now);
}
