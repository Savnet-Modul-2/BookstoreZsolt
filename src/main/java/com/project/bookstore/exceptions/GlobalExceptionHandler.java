package com.project.bookstore.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.NoSuchAlgorithmException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityAlreadyExistsException(EntityExistsException entityExistsException) {
        ErrorDetail errorDetail = new ErrorDetail(entityExistsException.getMessage());
        return new ResponseEntity<>(errorDetail, CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        ErrorDetail errorDetail = new ErrorDetail(entityNotFoundException.getMessage());
        return new ResponseEntity<>(errorDetail, NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<?> handleUserValidationException(EntityValidationException entityValidationException) {
        ErrorDetail errorDetail = new ErrorDetail(entityValidationException.getErrors());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(CodeExpirationTimeException.class)
    public ResponseEntity<?> handleCodeExpirationException(CodeExpirationTimeException codeExpirationTimeException) {
        ErrorDetail errorDetail = new ErrorDetail(codeExpirationTimeException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(EntityBadCredentialsException.class)
    public ResponseEntity<?> handleUserBadCredentialsException(EntityBadCredentialsException entityBadCredentialsException) {
        ErrorDetail errorDetail = new ErrorDetail(entityBadCredentialsException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(EntityAccountNotVerifiedException.class)
    public ResponseEntity<?> handleUserAccountNotVerifiedException(EntityAccountNotVerifiedException entityAccountNotVerifiedException) {
        ErrorDetail errorDetail = new ErrorDetail(entityAccountNotVerifiedException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchAlgorithmWrapperException.class)
    public ResponseEntity<?> handleNoSuchAlgorithmException(NoSuchAlgorithmWrapperException noSuchAlgorithmWrapperException) {
        ErrorDetail errorDetail = new ErrorDetail(noSuchAlgorithmWrapperException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotVerifiedException.class)
    public ResponseEntity<?> handleEntityNotVerifiedException(EntityNotVerifiedException entityNotVerifiedException) {
        ErrorDetail errorDetail = new ErrorDetail(entityNotVerifiedException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(RequestBodyMapKeyNotFoundException.class)
    public ResponseEntity<?> handleRequestBodyMapKeyNotFoundException(RequestBodyMapKeyNotFoundException requestBodyMapKeyNotFoundException) {
        ErrorDetail errorDetail = new ErrorDetail(requestBodyMapKeyNotFoundException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        ErrorDetail errorDetail = new ErrorDetail(illegalArgumentException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

}
