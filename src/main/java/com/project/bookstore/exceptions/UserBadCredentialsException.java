package com.project.bookstore.exceptions;

public class UserBadCredentialsException extends RuntimeException {
    public UserBadCredentialsException(String message) {
        super(message);
    }
}
