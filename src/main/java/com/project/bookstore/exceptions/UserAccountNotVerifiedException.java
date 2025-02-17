package com.project.bookstore.exceptions;

public class UserAccountNotVerifiedException extends RuntimeException {
    public UserAccountNotVerifiedException(String message) {
        super(message);
    }
}
