package com.project.bookstore.controller;

import com.project.bookstore.dto.BookExemplarsToCreateDto;
import com.project.bookstore.dto.ReservationDto;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.mapper.BookExemplarMapper;
import com.project.bookstore.mapper.ReservationMapper;
import com.project.bookstore.service.BookExemplarService;
import com.project.bookstore.validator.BookExemplarValidator;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exemplars")
public class BookExemplarController {
    @Autowired
    private BookExemplarService bookExemplarService;
    @Autowired
    private BookExemplarMapper bookExemplarMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private BookExemplarValidator bookExemplarValidator;
    @Autowired
    private ReservationValidator reservationValidator;

    @InitBinder({"bookExemplarsToCreateDto", "bookExemplarsDto", "reservationDto"})
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(bookExemplarValidator, reservationValidator);
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> addBookExemplar(@PathVariable(name = "bookId") Long bookId,
                                             @Valid @RequestBody BookExemplarsToCreateDto bookExemplarDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        List<BookExemplar> bookExemplars = bookExemplarMapper.mapAndCreateBookExemplarsFromBookExemplarsDto(bookExemplarDto);
        return ResponseEntity.ok(bookExemplarMapper.mapBookExemplarsDtoListFromBookExemplarsList(bookExemplarService.createBookExemplars(bookId, bookExemplars)));
    }

    @GetMapping
    public ResponseEntity<?> getAllBookExemplars(@RequestParam(name = "pageSize", required = false) Integer pageSize,
                                                 @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        if (pageSize != null && pageNumber != null) {
            Page<BookExemplar> pageBookExemplary = bookExemplarService.findAll(PageRequest.of(pageNumber, pageSize));
            return ResponseEntity.ok(pageBookExemplary.map(bookExemplary -> bookExemplarMapper.mapBookExemplarDtoFromBookExemplar(bookExemplary)));
        }
        return ResponseEntity.ok(bookExemplarMapper.mapBookExemplarsDtoListFromBookExemplarsList(bookExemplarService.findAll()));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookExemplarsFromABookPaginated(@PathVariable(name = "bookId") Long bookId,
                                                                @RequestParam(name = "pageSize") Integer pageSize,
                                                                @RequestParam(name = "pageNumber") Integer pageNumber) {
        Page<BookExemplar> pageBookExemplar = bookExemplarService.findAll(bookId, PageRequest.of(pageNumber, pageSize));
        return ResponseEntity.ok(pageBookExemplar.map(bookExemplar -> bookExemplarMapper.mapBookExemplarDtoFromBookExemplar(bookExemplar)));
    }

    @GetMapping("/{bookId}/reserve")
    public ResponseEntity<?> getFirstAvailableBookExemplarToBeReserved(@PathVariable(name = "bookId") Long bookId,
                                                                       @Valid @RequestBody ReservationDto reservationDto,
                                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        BookExemplar bookExemplar = bookExemplarService.findFirstBookExemplarForReservation(bookId, reservationMapper.mapReservationFromReservationDto(reservationDto));
        return ResponseEntity.ok(bookExemplarMapper.mapBookExemplarDtoFromBookExemplar(bookExemplar));
    }

    @DeleteMapping("/{exemplarId}")
    public ResponseEntity<?> deleteExemplarById(@PathVariable(name = "exemplarId") Long exemplarId) {
        bookExemplarService.deleteById(exemplarId);
        return ResponseEntity.noContent().build();
    }
}
