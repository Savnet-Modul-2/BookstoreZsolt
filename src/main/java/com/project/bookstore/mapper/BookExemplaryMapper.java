package com.project.bookstore.mapper;

import com.project.bookstore.dto.BookExemplaryDto;
import com.project.bookstore.entity.BookExemplary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookExemplaryMapper {
    @Autowired
    private BookMapper bookMapper;

    public BookExemplary mapBookExemplaryFromBookExemplaryDto(BookExemplaryDto bookExemplaryDto) {
        BookExemplary bookExemplary = new BookExemplary();
        bookExemplary.setId(bookExemplaryDto.getId());
        bookExemplary.setPublisher(bookExemplaryDto.getPublisher());
        bookExemplary.setMaximumReservationDuration(bookExemplaryDto.getMaximumReservationDuration());
        return bookExemplary;
    }

    public BookExemplaryDto mapBookExemplaryDtoFromBookExemplary(BookExemplary bookExemplary) {
        BookExemplaryDto bookExemplaryDto = new BookExemplaryDto();
        bookExemplaryDto.setId(bookExemplary.getId());
        bookExemplaryDto.setPublisher(bookExemplary.getPublisher());
        bookExemplaryDto.setMaximumReservationDuration(bookExemplary.getMaximumReservationDuration());
        bookExemplaryDto.setBook(bookMapper.mapBookDtoFromBook(bookExemplary.getBook()));
        return bookExemplaryDto;
    }

    public List<BookExemplary> mapBookExemplarsListFromBookExemplarsDtoList(List<BookExemplaryDto> bookExemplaryDtoList) {
        return bookExemplaryDtoList.stream()
                .map(this::mapBookExemplaryFromBookExemplaryDto)
                .toList();
    }

    public List<BookExemplaryDto> mapBookExemplarsDtoListFromBookExemplarsList(List<BookExemplary> bookExemplaryList) {
        return bookExemplaryList.stream()
                .map(this::mapBookExemplaryDtoFromBookExemplary)
                .toList();
    }
}
