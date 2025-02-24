package com.project.bookstore.controller;


import com.project.bookstore.service.BookExemplaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exemplary")
public class BookExemplaryController {

    @Autowired
    private BookExemplaryService bookExemplaryService;

    @GetMapping
    public ResponseEntity<?> getAllBookExemplars(@RequestParam(name = "pageSize") int pageSize, @RequestParam(name = "pageNumber") int pageNumber) {
        if (pageSize != 0 && pageNumber != 0) {
            return ResponseEntity.ok(bookExemplaryService.getAllBookExemplaries(PageRequest.of(pageNumber, pageSize)));
        }
        return ResponseEntity.ok(bookExemplaryService.getAllBookExemplaries());
    }

    @DeleteMapping("/{exemplaryId}")
    public ResponseEntity<?> deleteExemplaryById(@PathVariable(name = "exemplaryID") Long exemplaryId) {
        bookExemplaryService.deleteExemplaryById(exemplaryId);
        return ResponseEntity.noContent().build();
    }

}
