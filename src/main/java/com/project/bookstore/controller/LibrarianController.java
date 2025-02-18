package com.project.bookstore.controller;

import com.project.bookstore.dto.LibrarianDto;
import com.project.bookstore.entity.Librarian;
import com.project.bookstore.mapper.LibrarianMapper;
import com.project.bookstore.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequestMapping("/librarians")
public class LibrarianController {
    @Autowired
    private LibrarianService librarianService;
    @Autowired
    private LibrarianMapper librarianMapper;

    @PostMapping
    public ResponseEntity<?> createLibrarian(@RequestBody LibrarianDto librarianDto) throws NoSuchAlgorithmException {
        Librarian createdLibrarian = librarianService.createLibrarian(librarianMapper.mapLibrarianFromLibrarianDto(librarianDto));
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoFromLibrarian(createdLibrarian));
    }

    @GetMapping("/{librarianId}")
    public ResponseEntity<?> getLibrarianById(@PathVariable(name = "librarianId") Long librarianId) {
        Librarian foundLibrarian = librarianService.getLibrarianById(librarianId);
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoFromLibrarian(foundLibrarian));
    }

    @GetMapping
    public ResponseEntity<?> getAllLibrarians() {
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoListFromLibrarianList(librarianService.getAllLibrarians()));
    }

    @DeleteMapping("/{librarianId}")
    public ResponseEntity<?> deleteLibrarianById(@PathVariable(name = "librarianId") Long librarianId) {
        librarianService.deleteLibrarianById(librarianId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{librarianId}")
    public ResponseEntity<?> verifyLibrarian(@PathVariable(name = "librarianId") Long librarianId, @RequestBody Map<String, String> codeMap) {
        Librarian verifiedLibrarian = librarianService.verifyLibrarian(librarianId, codeMap.get("verificationCode"));
        return ResponseEntity.ok(librarianMapper.mapLibrarianDtoFromLibrarian(verifiedLibrarian));
    }

}
