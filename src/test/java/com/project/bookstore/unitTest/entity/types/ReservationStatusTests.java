package com.project.bookstore.unitTest.entity.types;

import com.project.bookstore.entity.types.ReservationStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReservationStatusTests {
    private ReservationStatus reservationStatus;

    @Test
    public void givenInProgressWhenPending_IsNextStatePossible_ReturnTrue() {
        reservationStatus = ReservationStatus.PENDING;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.IN_PROGRESS)).isTrue();
    }

    @Test
    public void givenCancelledWhenPending_IsNextStatePossible_ReturnTrue(){
        reservationStatus = ReservationStatus.PENDING;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.CANCELLED)).isTrue();
    }

    @Test
    public void givenDelayedWhenPending_IsNextStatePossible_ReturnFalse() {
        reservationStatus = ReservationStatus.PENDING;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.DELAYED)).isFalse();
    }

    @Test
    public void givenDelayedWhenInProgress_IsNextStatePossible_ReturnTrue() {
        reservationStatus = ReservationStatus.IN_PROGRESS;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.DELAYED)).isTrue();
    }

    @Test
    public void givenFinishedWhenInProgress_IsNextStatePossible_ReturnTrue(){
        reservationStatus = ReservationStatus.IN_PROGRESS;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.FINISHED)).isTrue();
    }

    @Test
    public void givenCancelledWhenInProgress_IsNextStatePossible_ReturnFalse() {
        reservationStatus = ReservationStatus.IN_PROGRESS;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.CANCELLED)).isFalse();
    }

    @Test
    public void givenFinishedWhenDelayed_IsNextStatePossible_ReturnTrue() {
        reservationStatus = ReservationStatus.DELAYED;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.FINISHED)).isTrue();
    }

    @Test
    public void givenPendingWhenDelayed_IsNextStatePossible_ReturnFalse() {
        reservationStatus=ReservationStatus.DELAYED;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.PENDING)).isFalse();
    }

    @Test
    public void givenAnythingWhenFinished_IsNextStatePossible_ReturnFalse() {
        reservationStatus = ReservationStatus.FINISHED;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.PENDING)).isFalse();
    }

    @Test
    public void givenAnythingWhenCancelled_IsNextStatePossible_ReturnFalse() {
        reservationStatus = ReservationStatus.CANCELLED;

        Assertions.assertThat(reservationStatus.isNextStatePossible(ReservationStatus.PENDING)).isFalse();
    }
}
