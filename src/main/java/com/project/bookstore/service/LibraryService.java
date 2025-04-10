package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Library;
import com.project.bookstore.entity.User;
import com.project.bookstore.exceptions.EntityNotVerifiedException;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.LibraryRepository;
import com.project.bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public Library addBookToLibrary(Long id, Book book) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library with id %s not found".formatted(id)));
        if (!library.getLibrarian().isVerifiedAccount()) {
            throw new EntityNotVerifiedException("The user's account is not verified to perform this action");
        }
        library.addBook(book);
        return libraryRepository.save(library);
    }

    public Library findById(Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Library with id %s not found".formatted(id)));
    }

    public List<Library> findAll() {
        return libraryRepository.findAll();
    }

    public Library addExistingBookToLibrary(Long libraryId, Long bookId) {
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        Library foundLibrary = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id %s not found".formatted(libraryId)));
        if (!foundLibrary.getLibrarian().isVerifiedAccount()) {
            throw new EntityNotVerifiedException("The user's account is not verified to perform this action");
        }
        foundLibrary.addBook(foundBook);
        return libraryRepository.save(foundLibrary);
    }

    public Library updateLibrary(Long libraryId, Library library) {
        Library foundLibrary = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id %s not found".formatted(libraryId)));
        foundLibrary.setCity(library.getCity());
        foundLibrary.setName(library.getName());
        foundLibrary.setPhoneNumber(library.getPhoneNumber());
        return libraryRepository.save(foundLibrary);
    }

    public Library addLibraryToFavourite(Long userId, Long libraryId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
        Library foundLibrary = libraryRepository.findById(libraryId)
                .orElseThrow(() -> new EntityNotFoundException("Library with id  %s not found".formatted(libraryId)));
        if (!foundUser.getFavouriteLibraries().contains(foundLibrary)) {
            foundLibrary.addUserToFavourites(foundUser);
        } else {
            foundLibrary.removeUserFromFavourites(foundUser);
        }
        return libraryRepository.save(foundLibrary);
    }
}
