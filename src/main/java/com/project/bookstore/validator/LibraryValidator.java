package com.project.bookstore.validator;

import com.project.bookstore.dto.LibraryDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class LibraryValidator implements Validator {
    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{3}?[-. ]?(\\(?[0-9]{4}\\)?)?[-. ]?[0-9]{4}[-. ]?[0-9]{4}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return LibraryDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LibraryDto libraryDto = (LibraryDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.name", "library.name.required", "name field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.city", "library.city.required", "city field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.phoneNumber", "library.phoneNumber.required", "phoneNumber field is required");
        if (libraryDto.getPhoneNumber() != null && !Pattern.matches(PHONE_NUMBER_PATTERN, libraryDto.getPhoneNumber())) {
            errors.rejectValue("library.phoneNumber", "library.phoneNumber.invalid", "Invalid phoneNumber format");
        }
    }
}
