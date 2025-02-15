package com.project.bookstore.helper;

import com.project.bookstore.dto.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_NUMBER_PATTERN = "^\\+?[0-9]{3}?[-. ]?(\\(?[0-9]{4}\\)?)?[-. ]?[0-9]{4}[-. ]?[0-9]{4}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDTO = (UserDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");

        if (userDTO.getPhoneNumber() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNumber", "phoneNumber.required");
        } else if (!Pattern.matches(PHONE_NUMBER_PATTERN, userDTO.getPhoneNumber())) {
            errors.rejectValue("phoneNumber", "phoneNumber.invalid", "Invalid phone number format");
        }

        if (userDTO.getEmail() == null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required");
        } else if (!Pattern.matches(EMAIL_PATTERN, userDTO.getEmail())) {
            errors.rejectValue("email", "email.invalid", "Invalid email format");
        }

    }
}
