package com.project.bookstore.validator;

import com.project.bookstore.dto.LibrarianDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class LibrarianValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{3}?[-. ]?(\\(?[0-9]{4}\\)?)?[-. ]?[0-9]{4}[-. ]?[0-9]{4}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return LibrarianDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LibrarianDto librarianDto = (LibrarianDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "name field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "password field is required");

        if (librarianDto.getEmail() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "email field is required");
        } else if (!Pattern.matches(EMAIL_PATTERN, librarianDto.getEmail())) {
            errors.rejectValue("email", "email.invalid", "Invalid email format");
        }

        if (librarianDto.getLibrary() == null) {
            errors.rejectValue("library", "library.null", "library cannot be null");
        } else {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.name", "library.name.required", "library name field is required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.city", "library.city.required", "library city field is required");

            if (librarianDto.getLibrary().getPhoneNumber().isEmpty()) {
                ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.phoneNumber", "library.phoneNumber", "library phoneNumber field is required");
            } else if (!Pattern.matches(PHONE_NUMBER_PATTERN, librarianDto.getLibrary().getPhoneNumber())) {
                errors.rejectValue("library.phoneNumber", "library.phoneNumber.invalid", "Invalid library phoneNumber format");
            }
        }
    }
}
