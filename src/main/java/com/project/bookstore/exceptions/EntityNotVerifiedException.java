package com.project.bookstore.exceptions;

public class EntityNotVerifiedException extends RuntimeException {
    public EntityNotVerifiedException(String message) {
        super(message);
    }
}
