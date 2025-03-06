package com.project.bookstore.exceptions;

public class MaximumReservationDurationExceededException extends RuntimeException {
    public MaximumReservationDurationExceededException(String message) {
        super(message);
    }
}
