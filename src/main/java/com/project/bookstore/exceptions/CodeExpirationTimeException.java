package com.project.bookstore.exceptions;

public class CodeExpirationTimeException extends Exception{
    public CodeExpirationTimeException(String message) {
        super(message);
    }
}
