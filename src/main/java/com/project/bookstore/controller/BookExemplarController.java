package com.project.bookstore.controller;


import com.project.bookstore.dto.BookExemplarDto;
import com.project.bookstore.dto.BookExemplarsToCreateDto;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.mapper.BookExemplarMapper;
import com.project.bookstore.service.BookExemplarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exemplars")
public class BookExemplarController {

    @Autowired
    private BookExemplarService bookExemplarService;
    @Autowired
    private BookExemplarMapper bookExemplarMapper;

    @PostMapping("/{bookId}")
    public ResponseEntity<?> addBookExemplar(@PathVariable(name = "bookId") Long bookId, @RequestBody BookExemplarsToCreateDto bookExemplarDto) {
        List<BookExemplar> bookExemplars = bookExemplarMapper.mapAndCreateBookExemplarsFromBookExemplarsDto(bookExemplarDto);
        return ResponseEntity.ok(bookExemplarMapper.mapBookExemplarsDtoListFromBookExemplarsList(bookExemplarService.createBookExemplars(bookId, bookExemplars)));
    }

    @GetMapping
    public ResponseEntity<?> getAllBookExemplars(@RequestParam(name = "pageSize", required = false) Integer pageSize, @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        if (pageSize != null && pageNumber != null) {
            Page<BookExemplar> pageBookExemplary = bookExemplarService.findAll(PageRequest.of(pageNumber, pageSize));
            return ResponseEntity.ok(pageBookExemplary.map(bookExemplary -> bookExemplarMapper.mapBookExemplarDtoFromBookExemplar(bookExemplary)));
        }
        return ResponseEntity.ok(bookExemplarMapper.mapBookExemplarsDtoListFromBookExemplarsList(bookExemplarService.findAll()));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookExemplarsFromABookPaginated(@PathVariable(name = "bookId") Long bookId, @RequestParam(name = "pageSize") Integer pageSize, @RequestParam(name = "pageNumber") Integer pageNumber) {
        Page<BookExemplar> pageBookExemplar = bookExemplarService.findAll(bookId, PageRequest.of(pageNumber, pageSize));
        return ResponseEntity.ok(pageBookExemplar.map(bookExemplar -> bookExemplarMapper.mapBookExemplarDtoFromBookExemplar(bookExemplar)));
    }

    @DeleteMapping("/{exemplarId}")
    public ResponseEntity<?> deleteExemplarById(@PathVariable(name = "exemplarId") Long exemplarId) {
        bookExemplarService.deleteById(exemplarId);
        return ResponseEntity.noContent().build();
    }
}
