package com.project.bookstore.validator;

import com.project.bookstore.dto.ReservationDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ReservationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ReservationDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationDto reservationDto = (ReservationDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "startDate.required", "startDate field is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "endDate.required", "endDate field is required");
    }
}
