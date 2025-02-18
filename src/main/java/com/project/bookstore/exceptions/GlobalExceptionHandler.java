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

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<?> handleUserValidationException(UserValidationException userValidationException) {
        ErrorDetail errorDetail = new ErrorDetail(userValidationException.getErrors());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(CodeExpirationTimeException.class)
    public ResponseEntity<?> handleCodeExpirationException(CodeExpirationTimeException codeExpirationTimeException) {
        ErrorDetail errorDetail = new ErrorDetail(codeExpirationTimeException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(UserBadCredentialsException.class)
    public ResponseEntity<?> handleUserBadCredentialsException(UserBadCredentialsException userBadCredentialsException) {
        ErrorDetail errorDetail = new ErrorDetail(userBadCredentialsException.getMessage());
        return new ResponseEntity<>(errorDetail, CONFLICT);
    }

    @ExceptionHandler(UserAccountNotVerifiedException.class)
    public ResponseEntity<?> handleUserAccountNotVerifiedException(UserAccountNotVerifiedException userAccountNotVerifiedException) {
        ErrorDetail errorDetail = new ErrorDetail(userAccountNotVerifiedException.getMessage());
        return new ResponseEntity<>(errorDetail, BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseEntity<?>handleNoSuchAlgorithmException(NoSuchAlgorithmException noSuchAlgorithmException){
        ErrorDetail errorDetail=new ErrorDetail(noSuchAlgorithmException.getMessage());
        return new ResponseEntity<>(errorDetail,BAD_REQUEST);
    }

}
