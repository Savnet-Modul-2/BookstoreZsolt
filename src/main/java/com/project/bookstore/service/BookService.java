package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Library;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.LibraryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    //TODO:method never used in BookController
    public Library addBookToLibrary(Long libraryId, Book book) {
        Library foundLibrary = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id %s not found".formatted(libraryId)));
        foundLibrary.addBook(book);
        return libraryRepository.save(foundLibrary);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book updateBook(Long id, Book book) {
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        foundBook.setBookLanguage(book.getBookLanguage());
        foundBook.setGenre(book.getGenre());
        foundBook.setIsbn(book.getIsbn());
        foundBook.setAuthor(book.getAuthor());
        foundBook.setTitle(book.getTitle());
        foundBook.setNrOfPages(book.getNrOfPages());
        foundBook.setAppearanceDate(book.getAppearanceDate());
        return bookRepository.save(foundBook);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> findBooks(String title, String author) {
    //return new PageImpl<>()
    }
}
