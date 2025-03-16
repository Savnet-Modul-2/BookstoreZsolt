package com.project.bookstore.validator;

import com.project.bookstore.dto.BookExemplarDto;
import com.project.bookstore.dto.BookExemplarsToCreateDto;
import com.project.bookstore.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BookExemplarValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BookExemplarDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookExemplarDto bookExemplarDto = (BookExemplarDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "publisher", "publisher.required", "publisher field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "maximumReservationDuration", "maximumReservationDuration.required", "maximumReservationDuration field is required");
        if (bookExemplarDto.getMaximumReservationDuration() < 1) {
            errors.rejectValue("maximumReservationDuration", "maximumReservationDuration.invalid", "maximumReservationDuration value must be greater than 1");
        }
    }
}
