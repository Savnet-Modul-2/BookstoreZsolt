package com.project.bookstore.validator;

import com.project.bookstore.dto.ReservationStatusFilterDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReservationStatusFilterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ReservationStatusFilterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationStatusFilterDto reservationStatusFilterDto = (ReservationStatusFilterDto) target;
        if (reservationStatusFilterDto.getStartDate() != null && reservationStatusFilterDto.getEndDate() != null) {
            if (reservationStatusFilterDto.getStartDate().isAfter(reservationStatusFilterDto.getEndDate())) {
                errors.rejectValue("startDate", "startDate.invalid", "startDate cannot be after endDate");
            }
        }
    }
}