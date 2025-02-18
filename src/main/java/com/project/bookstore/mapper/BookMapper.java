package com.project.bookstore.mapper;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {

    public Book mapBookFromBookDto(BookDto bookDto){
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setAppearanceDate(bookDto.getAppearanceDate());
        book.setNrOfPages(bookDto.getNrOfPages());
        book.setGenre(bookDto.getGenre());
        book.setBookLanguage(bookDto.getBookLanguage());
        return book;
    }

    public BookDto mapBookDtoFromBook(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setAppearanceDate(book.getAppearanceDate());
        bookDto.setNrOfPages(book.getNrOfPages());
        bookDto.setGenre(book.getGenre());
        bookDto.setBookLanguage(book.getBookLanguage());
        return bookDto;
    }

    public List<Book> mapBookListFromBookDtoList(List<BookDto> bookDtoList) throws NoSuchAlgorithmException {
        List<Book> bookList = new ArrayList<>();
        for (BookDto bookDto : bookDtoList) {
            bookList.add(mapBookFromBookDto(bookDto));
        }
        return bookList;
    }

    public List<BookDto> mapBookDtoListFromBookList(List<Book> bookList) {
        return bookList.stream().map(this::mapBookDtoFromBook).toList();
    }

}
