package com.project.bookstore.exceptions;

import java.util.Map;

public class EntityValidationException extends RuntimeException {
    private Map<String, String> errors;

    public EntityValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
