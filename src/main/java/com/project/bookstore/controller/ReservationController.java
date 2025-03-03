package com.project.bookstore.controller;

import com.project.bookstore.dto.ReservationDto;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.mapper.ReservationMapper;
import com.project.bookstore.service.ReservationService;
import com.project.bookstore.validator.ReservationValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private ReservationValidator reservationValidator;

    @InitBinder({"reservationDto"})
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(reservationValidator);
    }

    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<?> reserveBook(@PathVariable(name = "bookId") Long bookId,
                                         @PathVariable(name = "userId") Long userId,
                                         @Valid @RequestBody ReservationDto reservationDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Reservation reservation = reservationService.reserveBook(userId, bookId, reservationMapper.mapReservationFromReservationDto(reservationDto));
        return ResponseEntity.ok(reservationMapper.mapReservationDtoFromReservation(reservation));
    }
}
