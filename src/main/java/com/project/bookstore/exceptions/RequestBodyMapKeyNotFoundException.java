package com.project.bookstore.exceptions;

public class RequestBodyMapKeyNotFoundException extends RuntimeException {
    public RequestBodyMapKeyNotFoundException(String message) {
        super(message);
    }
}
