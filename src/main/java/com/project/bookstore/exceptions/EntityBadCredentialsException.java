package com.project.bookstore.exceptions;

public class EntityBadCredentialsException extends RuntimeException {
    public EntityBadCredentialsException(String message) {
        super(message);
    }
}
