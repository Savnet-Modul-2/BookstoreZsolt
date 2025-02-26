package com.project.bookstore.mapper;

import com.project.bookstore.dto.BookExemplarDto;
import com.project.bookstore.entity.BookExemplar;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class BookExemplarMapper {

    public BookExemplar mapBookExemplaryFromBookExemplaryDto(BookExemplarDto bookExemplarDto) {
        BookExemplar bookExemplar = new BookExemplar();
        bookExemplar.setId(bookExemplarDto.getId());
        bookExemplar.setPublisher(bookExemplarDto.getPublisher());
        bookExemplar.setMaximumReservationDuration(bookExemplarDto.getMaximumReservationDuration());
        return bookExemplar;
    }

    public BookExemplarDto mapBookExemplaryDtoFromBookExemplary(BookExemplar bookExemplar) {
        BookExemplarDto bookExemplarDto = new BookExemplarDto();
        bookExemplarDto.setId(bookExemplar.getId());
        bookExemplarDto.setPublisher(bookExemplar.getPublisher());
        bookExemplarDto.setMaximumReservationDuration(bookExemplar.getMaximumReservationDuration());
        return bookExemplarDto;
    }

    public List<BookExemplar> mapAndCreateBookExemplarsFromBookExemplarsDto(BookExemplarDto bookExemplarDto) {
        return IntStream.range(0, bookExemplarDto.getNrOfExemplarsToCreate())
                .mapToObj(i -> mapBookExemplaryFromBookExemplaryDto(bookExemplarDto))
                .toList();
    }

    public List<BookExemplar> mapBookExemplarsListFromBookExemplarsDtoList(List<BookExemplarDto> bookExemplarDtoList) {
        return bookExemplarDtoList.stream()
                .map(this::mapBookExemplaryFromBookExemplaryDto)
                .toList();
    }

    public List<BookExemplarDto> mapBookExemplarsDtoListFromBookExemplarsList(List<BookExemplar> bookExemplarList) {
        return bookExemplarList.stream()
                .map(this::mapBookExemplaryDtoFromBookExemplary)
                .toList();
    }
}
