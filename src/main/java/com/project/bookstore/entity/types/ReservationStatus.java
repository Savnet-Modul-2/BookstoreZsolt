package com.project.bookstore.entity.types;

public enum ReservationStatus implements ReservationStatusStateMachine {
    PENDING {
        @Override
        public boolean isNextStatePossible(ReservationStatus reservationStatus) {
            return reservationStatus == IN_PROGRESS || reservationStatus == CANCELLED;
        }
    },
    IN_PROGRESS {
        @Override
        public boolean isNextStatePossible(ReservationStatus reservationStatus) {
            return reservationStatus == DELAYED || reservationStatus == FINISHED;
        }
    },
    DELAYED {
        @Override
        public boolean isNextStatePossible(ReservationStatus reservationStatus) {
            return reservationStatus == FINISHED;
        }
    },
    FINISHED {
        @Override
        public boolean isNextStatePossible(ReservationStatus reservationStatus) {
            return false;
        }
    },
    CANCELLED {
        @Override
        public boolean isNextStatePossible(ReservationStatus reservationStatus) {
            return false;
        }
    };

}
