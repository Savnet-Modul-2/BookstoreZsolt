package com.project.bookstore.repository;

import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservationStatus = :reservationStatus
            AND reservation.startDate < :now
            """)
    List<Reservation> searchExpiredPendingReservations(LocalDate now, ReservationStatus reservationStatus);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservationStatus = :reservationStatus
            AND  reservation.endDate < :now
            """)
    List<Reservation> searchExpiredDelayedReservations(LocalDate now, ReservationStatus reservationStatus);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE (reservation.startDate >= :startDate AND reservation.endDate <= :endDate)
            AND reservation.reservationStatus IN ('PENDING','DELAYED','IN_PROGRESS')
            AND reservation.reservedExemplar.book.library.id = :libraryId
            """)
    Page<Reservation> searchReservationsForALibraryByTimePeriod(Long libraryId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservedUser.id = :userId
            AND reservation.reservationStatus = :reservationStatus
            """)
    Page<Reservation> searchReservationsForAUserByReservationStatus(Long userId, ReservationStatus reservationStatus, Pageable pageable);
}
