package com.project.bookstore.mapper;

import com.project.bookstore.dto.BookExemplarDto;
import com.project.bookstore.dto.BookExemplarsToCreateDto;
import com.project.bookstore.dto.BookWithExemplarsDto;
import com.project.bookstore.entity.BookExemplar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class BookExemplarMapper {
    @Autowired
    private BookMapper bookMapper;

    public BookExemplar mapBookExemplarFromBookExemplarDto(BookExemplarsToCreateDto bookExemplarDto) {
        BookExemplar bookExemplar = new BookExemplar();
        bookExemplar.setPublisher(bookExemplarDto.getPublisher());
        bookExemplar.setMaximumReservationDuration(bookExemplarDto.getMaximumReservationDuration());
        return bookExemplar;
    }

    public BookExemplar mapBookExemplarFromBookExemplarDto(BookExemplarDto bookExemplarDto) {
        BookExemplar bookExemplar = new BookExemplar();
        bookExemplar.setPublisher(bookExemplarDto.getPublisher());
        bookExemplar.setMaximumReservationDuration(bookExemplarDto.getMaximumReservationDuration());
        return bookExemplar;
    }

    public BookExemplarDto mapBookExemplarDtoFromBookExemplar(BookExemplar bookExemplar) {
        BookExemplarDto bookExemplarDto = new BookExemplarDto();
        bookExemplarDto.setId(bookExemplar.getId());
        bookExemplarDto.setPublisher(bookExemplar.getPublisher());
        bookExemplarDto.setMaximumReservationDuration(bookExemplar.getMaximumReservationDuration());
        bookExemplarDto.setBook(bookMapper.mapBookDtoFromBook(bookExemplar.getBook()));
        return bookExemplarDto;
    }

    public List<BookExemplar> mapAndCreateBookExemplarsFromBookExemplarsDto(BookExemplarsToCreateDto bookExemplarDto) {
        return IntStream.range(0, bookExemplarDto.getNrOfExemplarsToCreate())
                .mapToObj(i -> mapBookExemplarFromBookExemplarDto(bookExemplarDto))
                .toList();
    }

    public List<BookExemplar> mapBookExemplarsListFromBookExemplarsDtoList(List<BookExemplarDto> bookExemplarDtoList) {
        return bookExemplarDtoList.stream()
                .map(this::mapBookExemplarFromBookExemplarDto)
                .toList();
    }

    public List<BookExemplarDto> mapBookExemplarsDtoListFromBookExemplarsList(List<BookExemplar> bookExemplarList) {
        return bookExemplarList.stream()
                .map(this::mapBookExemplarDtoFromBookExemplar)
                .toList();
    }
}
