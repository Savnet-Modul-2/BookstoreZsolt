package com.project.bookstore.mapper;

import com.project.bookstore.dto.LibraryDto;
import com.project.bookstore.entity.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibraryMapper {
    @Autowired
    private BookMapper bookMapper;

    public Library mapLibraryFromLibraryDto(LibraryDto libraryDto) {
        Library library = new Library();
        library.setId(libraryDto.getId());
        library.setName(libraryDto.getName());
        library.setCity(libraryDto.getCity());
        library.setPhoneNumber(libraryDto.getPhoneNumber());
        return library;
    }

    public LibraryDto mapLibraryDtoFromLibrary(Library library) {
        LibraryDto libraryDto = new LibraryDto();
        libraryDto.setId(library.getId());
        libraryDto.setName(library.getName());
        libraryDto.setCity(library.getCity());
        libraryDto.setPhoneNumber(library.getPhoneNumber());
        libraryDto.setBooks(library.getBooks().stream().map(bookMapper::mapBookDtoFromBook).toList());
        return libraryDto;
    }

    public List<Library> mapLibraryListFromLibraryDtoList(List<LibraryDto> libraryDtoList) {
        return libraryDtoList.stream().map(this::mapLibraryFromLibraryDto).toList();
    }

    public List<LibraryDto> mapLibraryDtoListFromLibraryList(List<Library> libraryList) {
        return libraryList.stream().map(this::mapLibraryDtoFromLibrary).toList();
    }
}
