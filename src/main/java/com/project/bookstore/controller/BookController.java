package com.project.bookstore.controller;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.mapper.BookWithExemplarsMapper;
import com.project.bookstore.dto.BookWithExemplarsDto;
import com.project.bookstore.entity.Book;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.service.BookService;
import com.project.bookstore.validator.BookValidator;
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
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BookWithExemplarsMapper bookWithExemplarsMapper;
    @Autowired
    private BookValidator bookValidator;

    @InitBinder({"bookDto", "bookWithExemplarsDto"})
    protected void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(bookValidator);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookWithExemplarsDto bookDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Book createdBook = bookService.createBook(bookWithExemplarsMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(createdBook));
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks(@RequestParam(name = "pageSize", required = false) Integer pageSize,
                                         @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        if (pageSize != null && pageNumber != null) {
            Page<Book> pageBook = bookService.findAll(PageRequest.of(pageNumber, pageSize));
            return ResponseEntity.ok(pageBook.map(book -> bookMapper.mapBookDtoFromBook(book)));
        }
        return ResponseEntity.ok(bookMapper.mapBookDtoListFromBookList(bookService.findAll()));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBook(@RequestParam(name = "title", required = false) String bookTitle,
                                        @RequestParam(name = "author", required = false) String bookAuthor,
                                        @RequestParam(name = "pageSize") Integer pageSize,
                                        @RequestParam(name = "pageNumber") Integer pageNumber) {
        if (pageSize == null || pageNumber == null) {
            throw new IllegalArgumentException("Missing pageSize and/or pageNumber values");
        }
        Page<Book> pageBook = bookService.findBooks(bookTitle, bookAuthor, PageRequest.of(pageNumber, pageSize));
        return ResponseEntity.ok(pageBook.map(book -> bookMapper.mapBookDtoFromBook(book)));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable(name = "bookId") Long bookId) {
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(bookService.findById(bookId)));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBookById(@PathVariable(name = "bookId") Long bookId,
                                            @Valid @RequestBody BookDto bookDto,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Book updatedBook = bookService.updateBook(bookId, bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(updatedBook));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable(name = "bookId") Long bookId) {
        bookService.deleteById(bookId);
        return ResponseEntity.noContent().build();
    }
}
