package com.project.bookstore.service;

import com.project.bookstore.entity.Library;
import com.project.bookstore.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;

    public Library getLibraryById(Long id) {
        return libraryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Library> getAllLibraries() {
        return libraryRepository.findAll();
    }

}
