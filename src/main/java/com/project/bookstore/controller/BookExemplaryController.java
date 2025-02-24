package com.project.bookstore.controller;


import com.project.bookstore.entity.BookExemplary;
import com.project.bookstore.mapper.BookExemplaryMapper;
import com.project.bookstore.service.BookExemplaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exemplary")
public class BookExemplaryController {

    @Autowired
    private BookExemplaryService bookExemplaryService;
    @Autowired
    private BookExemplaryMapper bookExemplaryMapper;

    @GetMapping
    public ResponseEntity<?> getAllBookExemplars(@RequestParam(name = "pageSize", required = false) Integer pageSize, @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        if (pageSize != null && pageNumber != null) {
            Page<BookExemplary> pageBookExemplary = bookExemplaryService.getAllBookExemplars(PageRequest.of(pageNumber, pageSize));
            return ResponseEntity.ok(pageBookExemplary.map(bookExemplary -> bookExemplaryMapper.mapBookExemplaryDtoFromBookExemplary(bookExemplary)));
        }
        return ResponseEntity.ok(bookExemplaryMapper.mapBookExemplarsDtoListFromBookExemplarsList(bookExemplaryService.getAllBookExemplars()));
    }

    @DeleteMapping("/{exemplaryId}")
    public ResponseEntity<?> deleteExemplaryById(@PathVariable(name = "exemplaryID") Long exemplaryId) {
        bookExemplaryService.deleteExemplaryById(exemplaryId);
        return ResponseEntity.noContent().build();
    }
}
