package com.project.bookstore.repository;

import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
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
            AND reservation.startDate <= :date
            """)
    List<Reservation> findByStartDateOlderThanAndStatus(LocalDate date, ReservationStatus reservationStatus);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservationStatus = :reservationStatus
            AND  reservation.endDate <= :date
            """)
    List<Reservation> findByEndDateOlderThanAndStatus(LocalDate date, ReservationStatus reservationStatus);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE (reservation.startDate >= :startDate AND reservation.endDate <= :endDate)
            AND (:reservationStatusList IS NULL OR reservation.reservationStatus IN :reservationStatusList)
            AND reservation.reservedExemplar.book.library.id = :libraryId
            """)
    Page<Reservation> findReservationsForALibraryByCriteria(Long libraryId, LocalDate startDate, LocalDate endDate, List<ReservationStatus> reservationStatusList, Pageable pageable);

    @Query("""
            SELECT reservation FROM reservation reservation
            WHERE reservation.reservedUser.id = :userId
            AND (cast (:startDate AS date) IS NULL OR reservation.startDate >= :startDate)
            AND (cast (:endDate AS date) IS NULL OR reservation.endDate <= :endDate)
            AND (:reservationStatusList IS NULL OR reservation.reservationStatus IN :reservationStatusList)
            """)
    Page<Reservation> findReservationsForAUserByCriteria(Long userId, LocalDate startDate, LocalDate endDate, List<ReservationStatus> reservationStatusList, Pageable pageable);
}
