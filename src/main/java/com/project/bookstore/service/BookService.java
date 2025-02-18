package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Library;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryRepository libraryRepository;

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Library addBookToLibrary(Long libraryId, Book book) {
        Library foundLibrary = libraryRepository.findById(libraryId).orElseThrow(EntityNotFoundException::new);
        foundLibrary.addBook(book);
        return libraryRepository.save(foundLibrary);

    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book book) {
        Book foundBook = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return foundBook;
    }

}
