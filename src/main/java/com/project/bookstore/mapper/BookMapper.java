package com.project.bookstore.mapper;

import com.project.bookstore.dto.BookDto;
import com.project.bookstore.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class BookMapper {
    @Autowired
    private BookExemplarMapper bookExemplarMapper;

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
        if (bookDto.getBookExemplary() != null) {
            book.setBookExemplars(IntStream.range(0, bookDto.getBookExemplary().getNrOfExemplarsToCreate())
                    .mapToObj(i -> bookExemplarMapper.mapBookExemplaryFromBookExemplaryDto(bookDto.getBookExemplary()))
                    .toList());
            book.getBookExemplars().forEach(bookExemplary -> bookExemplary.setBook(book));
        }
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
        bookDto.setBookExemplars(bookExemplarMapper.mapBookExemplarsDtoListFromBookExemplarsList(book.getBookExemplars()));
        return bookDto;
    }

    public List<Book> mapBookListFromBookDtoList(List<BookDto> bookDtoList) {
        return bookDtoList.stream().map(this::mapBookFromBookDto).toList();
    }

    public List<BookDto> mapBookDtoListFromBookList(List<Book> bookList) {
        return bookList.stream().map(this::mapBookDtoFromBook).toList();
    }
}
