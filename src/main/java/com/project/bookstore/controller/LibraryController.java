package com.project.bookstore.controller;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.BookWithExemplarsDto;
import com.project.bookstore.dto.LibraryDto;
import com.project.bookstore.entity.Library;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.mapper.BookWithExemplarsMapper;
import com.project.bookstore.mapper.LibraryMapper;
import com.project.bookstore.service.LibraryService;
import com.project.bookstore.validator.BookValidator;
import com.project.bookstore.validator.BookWithExemplarsValidator;
import com.project.bookstore.validator.LibraryValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private LibraryMapper libraryMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookWithExemplarsMapper bookWithExemplarsMapper;
    @Autowired
    private BookValidator bookValidator;
    @Autowired
    private LibraryValidator libraryValidator;
    @Autowired
    private BookWithExemplarsValidator bookWithExemplarsValidator;

    @InitBinder({"bookDto", "libraryDto", "bookWithExemplarsDto"})
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(libraryValidator, bookValidator, bookWithExemplarsValidator);
    }

    @PostMapping("/{libraryId}/addBook")
    public ResponseEntity<?> addBookToLibrary(@PathVariable(name = "libraryId") Long libraryId,
                                              @Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Library library = libraryService.addBookToLibrary(libraryId, bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(library));
    }

    @PostMapping("/{libraryId}/addBookWithExemplars")
    public ResponseEntity<?> addBookToLibrary(@PathVariable(name = "libraryId") Long libraryId,
                                              @Valid @RequestBody BookWithExemplarsDto bookWithExemplarsDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Library library = libraryService.addBookToLibrary(libraryId, bookWithExemplarsMapper.mapBookFromBookDto(bookWithExemplarsDto));
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(library));
    }

    @GetMapping("/{libraryId}")
    public ResponseEntity<?> getLibraryById(@PathVariable(name = "libraryId") Long libraryId) {
        Library foundLibrary = libraryService.findById(libraryId);
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(foundLibrary));
    }

    @GetMapping()
    public ResponseEntity<?> getAllLibraries() {
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoListFromLibraryList(libraryService.findAll()));
    }

    @PutMapping("/{libraryId}")
    public ResponseEntity<?> updateLibraryById(@PathVariable(name = "libraryId") Long libraryId,
                                               @Valid @RequestBody LibraryDto libraryDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Library updatedLibrary = libraryService.updateLibrary(libraryId, libraryMapper.mapLibraryFromLibraryDto(libraryDto));
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(updatedLibrary));
    }

    @PutMapping("/{libraryId}/{bookId}")
    public ResponseEntity<?> addExistingBookToLibrary(@PathVariable(name = "libraryId") Long libraryId,
                                                      @PathVariable(name = "bookId") Long bookId) {
        Library library = libraryService.addExistingBookToLibrary(libraryId, bookId);
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(library));
    }
}
