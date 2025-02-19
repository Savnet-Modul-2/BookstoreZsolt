package com.project.bookstore.controller;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.entity.Book;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookMapper bookMapper;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto) {
        Book createdBook = bookService.createBook(bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(createdBook));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable(name = "bookId") Long bookId) {
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(bookService.getBookById(bookId)));
    }

    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        return ResponseEntity.ok(bookMapper.mapBookDtoListFromBookList((bookService.getAllBooks())));
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable(name = "bookId") Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBookById(@PathVariable(name = "bookId") Long bookId, @RequestBody BookDto bookDto) {
        Book updatedBook = bookService.updateBookById(bookId, bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(bookMapper.mapBookDtoFromBook(updatedBook));
    }

    @GetMapping("/page/{numberOfBooks}")
    public ResponseEntity<?> getAllBookPaginated(@PathVariable(name = "numberOfBooks") int numberOfBooks) {
        Pageable pageWithNumberOfBooks = PageRequest.of(0, numberOfBooks);
        Page<Book> foundBooks = bookService.getAllBookPaginated(pageWithNumberOfBooks);
        return ResponseEntity.ok(foundBooks.map(book -> bookMapper.mapBookDtoFromBook(book)));
    }
}
