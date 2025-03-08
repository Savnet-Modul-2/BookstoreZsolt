package com.project.bookstore.controller;

import com.project.bookstore.dto.LibrarianDto;
import com.project.bookstore.entity.Librarian;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.entity.types.ReservationStatus;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.exceptions.RequestBodyMapKeyNotFoundException;
import com.project.bookstore.mapper.LibrarianMapper;
import com.project.bookstore.service.LibrarianService;
import com.project.bookstore.validator.LibrarianValidator;
import com.project.bookstore.validator.LibraryValidator;
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
@RequestMapping("/librarians")
public class LibrarianController {
    @Autowired
    private LibrarianService librarianService;
    @Autowired
    private LibrarianMapper librarianMapper;
    @Autowired
    private LibrarianValidator librarianValidator;
    @Autowired
    private LibraryValidator libraryValidator;

    @InitBinder({"librarianDto", "libraryDto"})
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(librarianValidator);
    }

    @PostMapping
    public ResponseEntity<?> createLibrarian(@Valid @RequestBody LibrarianDto librarianDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Librarian createdLibrarian = librarianService.createLibrarian(librarianMapper.mapLibrarianFromLibrarianDto(librarianDto));
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoFromLibrarian(createdLibrarian));
    }

    @GetMapping("/{librarianId}")
    public ResponseEntity<?> getLibrarianById(@PathVariable(name = "librarianId") Long librarianId) {
        Librarian foundLibrarian = librarianService.findById(librarianId);
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoFromLibrarian(foundLibrarian));
    }

    @GetMapping
    public ResponseEntity<?> getAllLibrarians() {
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoListFromLibrarianList(librarianService.findAll()));
    }

    @PutMapping("/{librarianId}")
    public ResponseEntity<?> verifyLibrarian(@PathVariable(name = "librarianId") Long librarianId,
                                             @RequestBody Map<String, String> codeMap) {
        if (!codeMap.containsKey("verificationCode")) {
            throw new RequestBodyMapKeyNotFoundException("Missing key on the request body");
        }
        Librarian verifiedLibrarian = librarianService.verifyLibrarian(librarianId, codeMap.get("verificationCode"));
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoFromLibrarian(verifiedLibrarian));
    }

    @PutMapping("/login")
    public ResponseEntity<?> loginIntoAccount(@RequestBody Map<String, String> loginCredentials) {
        if (!loginCredentials.containsKey("email") || !loginCredentials.containsKey("password")) {
            throw new RequestBodyMapKeyNotFoundException("Missing key on the request body");
        }
        Long id = librarianService.getLibrarianIdAfterLogin(loginCredentials.get("email"), loginCredentials.get("password"));
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{librarianId}")
    public ResponseEntity<?> deleteLibrarianById(@PathVariable(name = "librarianId") Long librarianId) {
        librarianService.deleteById(librarianId);
        return ResponseEntity.noContent().build();
    }
}
