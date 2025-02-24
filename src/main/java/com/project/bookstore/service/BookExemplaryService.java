package com.project.bookstore.service;

import com.project.bookstore.entity.BookExemplary;
import com.project.bookstore.repository.BookExemplaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookExemplaryService {
    @Autowired
    private BookExemplaryRepository bookExemplaryRepository;

    public List<BookExemplary> getAllBookExemplaries() {
        return bookExemplaryRepository.findAll();
    }

    public Page<BookExemplary> getAllBookExemplaries(Pageable pageable) {
        return bookExemplaryRepository.findAll(pageable);
    }

    public void deleteExemplaryById(Long id) {
        bookExemplaryRepository.deleteById(id);
    }


}
