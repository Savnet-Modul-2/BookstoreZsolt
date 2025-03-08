package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.BookExemplar;
import com.project.bookstore.entity.Reservation;
import com.project.bookstore.exceptions.BookExemplarNotAvailableException;
import com.project.bookstore.repository.BookExemplarRepository;
import com.project.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookExemplarService {
    @Autowired
    private BookExemplarRepository bookExemplarRepository;
    @Autowired
    private BookRepository bookRepository;

    public List<BookExemplar> createBookExemplars(Long id, List<BookExemplar> bookExemplars) {
        Book foundBook = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        bookExemplars.forEach(bookExemplar -> bookExemplar.setBook(foundBook));
        return bookExemplarRepository.saveAll(bookExemplars);
    }

    public BookExemplar findFirstBookExemplarForReservation(Long bookId, Reservation reservation) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return bookExemplarRepository.findFirstExemplarAvailable(book.getId(), reservation.getStartDate(), reservation.getEndDate())
                .orElseThrow(() -> new BookExemplarNotAvailableException("Book exemplar cannot be reserved for the given period"));
    }

    public List<BookExemplar> findAll() {
        return bookExemplarRepository.findAll();
    }

    public Page<BookExemplar> findAll(Pageable pageable) {
        return bookExemplarRepository.findAll(pageable);
    }

    public Page<BookExemplar> findAll(Long id, Pageable pageable) {
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), foundBook.getBookExemplars().size());
        return new PageImpl<>(foundBook.getBookExemplars().subList(start, end), pageable, foundBook.getBookExemplars().size());
    }

    public void deleteById(Long id) {
        bookExemplarRepository.deleteById(id);
    }
}
