package com.project.bookstore.exceptions;

public class CodeExpirationTimeException extends RuntimeException{
    public CodeExpirationTimeException(String message) {
        super(message);
    }
}
