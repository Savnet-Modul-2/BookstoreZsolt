package com.project.bookstore.validator;

import com.project.bookstore.dto.BookExemplarsToCreateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BookExemplarsToCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookExemplarsToCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookExemplarsToCreateDto bookExemplarsToCreateDto = (BookExemplarsToCreateDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publisher", "publisher.required", "publisher field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nrOfExemplarsToCreate", "nrOfExemplarsToCreate.required", "nrOfExemplarsToCreate field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maximumReservationDuration", "maximumReservationDuration.required", "maximumReservationDuration field is required");
        if (bookExemplarsToCreateDto.getMaximumReservationDuration() < 1) {
            errors.rejectValue("maximumReservationDuration", "maximumReservationDuration.invalid", "maximumReservationDuration value must be greater than 1");
        }
        if (bookExemplarsToCreateDto.getNrOfExemplarsToCreate() < 1) {
            errors.rejectValue("nrOfExemplarsToCreate", "nrOfExemplarsToCreate.invalid", "nrOfExemplarsToCreate value must be greater than 0");
        }
    }
}
