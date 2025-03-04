package com.project.bookstore.exceptions;

public class UnavailableStatusChangeException extends RuntimeException {

    public UnavailableStatusChangeException(String message) {
        super(message);
    }
}
