package com.project.bookstore.dto;

import com.project.bookstore.entity.Book;
import com.project.bookstore.mapper.BookExemplarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class BookWithExemplarsMapper {
    @Autowired
    private BookExemplarMapper bookExemplarMapper;

    public Book mapBookFromBookDto(BookWithExemplarsDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setAppearanceDate(bookDto.getAppearanceDate());
        book.setNrOfPages(bookDto.getNrOfPages());
        book.setGenre(bookDto.getGenre());
        book.setBookLanguage(bookDto.getBookLanguage());
        if (bookDto.getBookExemplar() != null) {
            book.setBookExemplars(IntStream.range(0, bookDto.getBookExemplar().getNrOfExemplarsToCreate())
                    .mapToObj(i -> bookExemplarMapper.mapBookExemplarFromBookExemplarDto(bookDto.getBookExemplar()))
                    .collect(Collectors.toList()));
            book.getBookExemplars().forEach(bookExemplary -> bookExemplary.setBook(book));
        }
        return book;
    }

    public List<Book> mapBookListFromBookDtoList(List<BookWithExemplarsDto> bookDtoList) {
        return bookDtoList.stream().map(this::mapBookFromBookDto).toList();
    }


}
