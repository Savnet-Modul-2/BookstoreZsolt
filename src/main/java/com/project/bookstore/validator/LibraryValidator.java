package com.project.bookstore.validator;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.BookWithExemplarsDto;
import com.project.bookstore.dto.LibraryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class LibraryValidator implements Validator {
    @Autowired
    private BookValidator bookValidator;

    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{3}?[-. ]?(\\(?[0-9]{4}\\)?)?[-. ]?[0-9]{4}[-. ]?[0-9]{4}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return LibraryDto.class.equals(clazz) || BookDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof LibraryDto libraryDto) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.name", "library.name.required", "name field is required");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "library.city", "library.city.required", "city field is required");

            if (libraryDto.getPhoneNumber() == null) {
                errors.rejectValue("phoneNumber", "phoneNumber.required", "phoneNumber field is required");
            } else if (!Pattern.matches(PHONE_NUMBER_PATTERN, libraryDto.getPhoneNumber())) {
                errors.rejectValue("phoneNumber", "phoneNumber.invalid", "Invalid phoneNumber format");
            }
        } else if(target instanceof BookDto){
            bookValidator.validate(target,errors);
        }
    }
}
