package com.project.bookstore.controller;

import com.project.bookstore.dto.ReservationDto;
import com.project.bookstore.dto.ReservationStatusFilterDto;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.exceptions.RequestBodyMapKeyNotFoundException;
import com.project.bookstore.mapper.ReservationMapper;
import com.project.bookstore.service.ReservationService;
import com.project.bookstore.validator.ReservationValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @InitBinder({"reservationDto", "reservationStatusFilterDto"})
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

    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        return ResponseEntity.ok(reservationMapper.mapReservationDtoListFromReservationList(reservationService.findAllReservations()));
    }

    @GetMapping("/library/{libraryId}")
    public ResponseEntity<?> getAllReservationsForALibraryByCriteria(@PathVariable(name = "libraryId") Long libraryId,
                                                                     @RequestParam(name = "pageSize") Integer pageSize,
                                                                     @RequestParam(name = "pageNumber") Integer pageNumber,
                                                                     @Valid @RequestBody ReservationStatusFilterDto reservationStatusFilterDto,
                                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Page<Reservation> reservationPage = reservationService.findReservationsForALibraryByCriteria(libraryId, reservationStatusFilterDto.getStartDate(), reservationStatusFilterDto.getEndDate(), reservationStatusFilterDto.getReservationStatusList(), PageRequest.of(pageNumber, pageSize));
        return ResponseEntity.ok(reservationPage.map(reservation -> reservationMapper.mapReservationDtoFromReservation(reservation)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllReservationsForAUserByCriteria(@PathVariable(name = "userId") Long userId,
                                                                  @RequestParam(name = "pageSize") Integer pageSize,
                                                                  @RequestParam(name = "pageNumber") Integer pageNumber,
                                                                  @Valid @RequestBody ReservationStatusFilterDto reservationStatusFilterDto,
                                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Page<Reservation> reservationPage = reservationService.findReservationsForAUserByCriteria(userId, reservationStatusFilterDto.getStartDate(), reservationStatusFilterDto.getEndDate(), reservationStatusFilterDto.getReservationStatusList(), PageRequest.of(pageNumber, pageSize));
        return ResponseEntity.ok(reservationPage.map(reservation -> reservationMapper.mapReservationDtoFromReservation(reservation)));
    }

    @PutMapping("/{librarianId}/{reservationId}")
    public ResponseEntity<?> updateReservationStatus(@PathVariable(name = "librarianId") Long librarianId,
                                                     @PathVariable(name = "reservationId") Long reservationId,
                                                     @RequestBody Map<String, String> reservationStatusMap) {
        if (!reservationStatusMap.containsKey("reservationStatus")) {
            throw new RequestBodyMapKeyNotFoundException("Missing reservationStatus property");
        }
        Reservation updatedReservation = reservationService.updateReservationStatus(librarianId, reservationId, ReservationStatus.valueOf(reservationStatusMap.get("reservationStatus")));
        return ResponseEntity.ok(reservationMapper.mapReservationDtoFromReservation(updatedReservation));
    }
}
