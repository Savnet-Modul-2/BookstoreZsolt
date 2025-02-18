package com.project.bookstore.controller;

import com.project.bookstore.dto.LibraryDto;
import com.project.bookstore.entity.Library;
import com.project.bookstore.mapper.LibraryMapper;
import com.project.bookstore.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private LibraryMapper libraryMapper;

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
        return ResponseEntity.noContent().build();
    }
}
