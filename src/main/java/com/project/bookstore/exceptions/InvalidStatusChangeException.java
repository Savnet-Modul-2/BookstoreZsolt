package com.project.bookstore.exceptions;

public class InvalidStatusChangeException extends RuntimeException {

    public InvalidStatusChangeException(String message) {
        super(message);
    }
}
