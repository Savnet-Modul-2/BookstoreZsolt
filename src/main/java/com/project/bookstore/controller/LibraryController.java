package com.project.bookstore.controller;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.dto.LibraryDto;
import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Library;
import com.project.bookstore.mapper.BookMapper;
import com.project.bookstore.mapper.LibraryMapper;
import com.project.bookstore.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private LibraryMapper libraryMapper;
    @Autowired
    private BookMapper bookMapper;

    @PostMapping("/{libraryId}/addBook")
    public ResponseEntity<?> addBookToLibrary(@PathVariable(name = "libraryId") Long libraryId, @RequestBody BookDto bookDto) {
        Library library = libraryService.addBookToLibrary(libraryId, bookMapper.mapBookFromBookDto(bookDto));
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(library));
    }

    @GetMapping("/{libraryId}")
    public ResponseEntity<?> getLibraryById(@PathVariable(name = "libraryId") Long libraryId) {
        Library foundLibrary = libraryService.getLibraryById(libraryId);
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(foundLibrary));
    }

    @GetMapping()
    public ResponseEntity<?> getAllLibraries() {
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoListFromLibraryList(libraryService.getAllLibraries()));
    }

    @PutMapping("/{libraryId}")
    public ResponseEntity<?> updateLibraryById(@PathVariable(name = "libraryId") Long libraryId, @RequestBody LibraryDto libraryDto) {
        Library updatedLibrary = libraryService.updateLibraryById(libraryId, libraryMapper.mapLibraryFromLibraryDto(libraryDto));
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(updatedLibrary));
    }

    @PutMapping("/{libraryId}/{bookId}")
    public ResponseEntity<?> addExistingBookToLibrary(@PathVariable(name = "libraryId") Long libraryId, @PathVariable(name = "bookId") Long bookId) {
        Library library = libraryService.addExistingBookToLibrary(libraryId, bookId);
        return ResponseEntity.ok(libraryMapper.mapLibraryDtoFromLibrary(library));
    }
}
