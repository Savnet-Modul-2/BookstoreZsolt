package com.project.bookstore.controller;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.entity.Book;
import com.project.bookstore.exceptions.EntityValidationException;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.service.BookService;
import com.project.bookstore.validator.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private BookValidator bookValidator;

    @InitBinder("bookDto")
    protected void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(bookValidator);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDto bookDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Book createdBook = bookService.createBook(bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(createdBook));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable(name = "bookId") Long bookId) {
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(bookService.findBookById(bookId)));
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(bookMapper.mapBookDtoListFromBookList((bookService.findAllBooks())));
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable(name = "bookId") Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBookById(@PathVariable(name = "bookId") Long bookId,@Valid @RequestBody BookDto bookDto,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new EntityValidationException(errorMap);
        }
        Book updatedBook = bookService.updateBookById(bookId, bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(updatedBook));
    }

    @GetMapping("/page/{numberOfBooks}")
    public ResponseEntity<?> getAllBookPaginated(@PathVariable(name = "numberOfBooks") int numberOfBooks) {
        Pageable pageWithNumberOfBooks = PageRequest.of(0, numberOfBooks);
        Page<Book> foundBooks = bookService.findAllBooksPaginated(pageWithNumberOfBooks);
        return ResponseEntity.ok(foundBooks.map(book -> bookMapper.mapBookDtoFromBook(book)));
    }
}
