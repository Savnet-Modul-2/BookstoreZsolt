package com.project.bookstore.exceptions;

public class UnauthorizedLibrarianAccessException extends RuntimeException {
    public UnauthorizedLibrarianAccessException(String message) {
        super(message);
    }
}
