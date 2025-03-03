package com.project.bookstore.validator;

import com.project.bookstore.dto.BookExemplarDto;
import com.project.bookstore.dto.BookExemplarsToCreateDto;
import com.project.bookstore.dto.ReservationDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BookExemplarValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookExemplarDto.class.equals(clazz) || BookExemplarsToCreateDto.class.equals(clazz) || ReservationDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof BookExemplarDto) {
            BookExemplarDto bookExemplarDto = (BookExemplarDto) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bookExemplar.publisher", "bookExemplar.publisher.required", "publisher field is required");
            if (bookExemplarDto.getMaximumReservationDuration() < 1) {
                errors.rejectValue("bookExemplar.maximumReservationDuration", "bookExemplar.maximumReservationDuration.invalid", "maximumReservationDuration value must be greater than 1");
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bookExemplar.maximumReservationDuration", "bookExemplar.maximumReservationDuration.required", "maximumReservationDuration field is required");
        } else if (target instanceof BookExemplarsToCreateDto) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publisher", "publisher.required", "publisher field is required");
            BookExemplarsToCreateDto bookExemplarsToCreateDto = (BookExemplarsToCreateDto) target;

            if (bookExemplarsToCreateDto.getMaximumReservationDuration() < 1) {
                errors.rejectValue("maximumReservationDuration", "maximumReservationDuration.invalid", "maximumReservationDuration value must be greater than 1");
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maximumReservationDuration", "maximumReservationDuration.required", "maximumReservationDuration field is required");
            if (bookExemplarsToCreateDto.getNrOfExemplarsToCreate() < 1) {
                errors.rejectValue("nrOfExemplarsToCreate", "nrOfExemplarsToCreate.invalid", "nrOfExemplarsToCreate value must be greater than 0");
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nrOfExemplarsToCreate", "nrOfExemplarsToCreate.required", "nrOfExemplarsToCreate field is required");
        }
    }
}
