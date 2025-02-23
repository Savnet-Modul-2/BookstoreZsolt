package com.project.bookstore.mapper;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public Book mapBookFromBookDto(BookDto bookDto) {
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

    public List<Book> mapBookListFromBookDtoList(List<BookDto> bookDtoList) {
        return bookDtoList.stream().map(this::mapBookFromBookDto).toList();
    }

    public List<BookDto> mapBookDtoListFromBookList(List<Book> bookList) {
        return bookList.stream().map(this::mapBookDtoFromBook).toList();
    }
}
