package com.project.bookstore.validator;

import com.project.bookstore.dto.LibrarianDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class LibrarianValidator implements Validator {
    @Autowired
    private LibraryValidator libraryValidator;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public boolean supports(Class<?> clazz) {
        return LibrarianDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LibrarianDto librarianDto = (LibrarianDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "name field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "email field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "password field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library", "library.required", "library field is required");
        if (!Pattern.matches(EMAIL_PATTERN, librarianDto.getEmail())) {
            errors.rejectValue("email", "email.invalid", "Invalid email format");
        }
        if (librarianDto.getLibrary() == null) {
        } else {
            libraryValidator.validate(librarianDto.getLibrary(), errors);
        }
    }
}
