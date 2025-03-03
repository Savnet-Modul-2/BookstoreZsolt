package com.project.bookstore.exceptions;

public class BookExemplarNotAvailableException extends RuntimeException {
    public BookExemplarNotAvailableException(String message) {
        super(message);
    }
}
