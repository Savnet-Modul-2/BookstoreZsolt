package com.project.bookstore.configuration;

import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.repository.ReservationRepository;
import com.project.bookstore.service.LibrarianService;
import com.project.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
public class ReservationScheduler {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private LibrarianService librarianService;
    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 22 * * *")
    public void reminderSetCancelledReservations() {
        List<Reservation> reservationList = reservationRepository.searchExpiredPendingReservations(LocalDate.now(), ReservationStatus.PENDING);
        reservationList.forEach(reservation -> reservation.setReservationStatus(ReservationStatus.CANCELLED));
        reservationRepository.saveAll(reservationList);
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void reminderSetDelayedReservations() {
        List<Reservation> reservationList = reservationRepository.searchExpiredDelayedReservations(LocalDate.now(), ReservationStatus.IN_PROGRESS);
        reservationList.forEach(reservation -> reservation.setReservationStatus(ReservationStatus.DELAYED));
        reservationRepository.saveAll(reservationList);
        librarianService.sendDelayedReservationEmail(reservationList);
        userService.sendDelayedReservationEmail(reservationList);
    }
}




