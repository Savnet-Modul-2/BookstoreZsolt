package com.project.bookstore.entity.types;

import com.project.bookstore.entity.Reservation;

public interface ReservationStatusStateMachine {
    boolean isNextStatePossible(ReservationStatus reservationStatus);
}
