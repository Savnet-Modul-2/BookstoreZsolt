package com.project.bookstore.mapper;

import com.project.bookstore.dto.LibrarianDto;
import com.project.bookstore.entity.Librarian;
import com.project.bookstore.helper.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibrarianMapper {
    @Autowired
    private LibraryMapper libraryMapper;

    public Librarian mapLibrarianFromLibrarianDto(LibrarianDto librarianDto) {
        Librarian librarian = new Librarian();
        librarian.setId(librarianDto.getId());
        librarian.setName(librarianDto.getName());
        librarian.setEmail(librarianDto.getEmail());
        librarian.setPassword(PasswordEncryptor.encryptPasswordWithSHA256(librarianDto.getPassword()));
        librarian.setLibrary(libraryMapper.mapLibraryFromLibraryDto(librarianDto.getLibrary()));
        librarian.setVerifiedAccount(librarianDto.isVerifiedAccount());
        librarian.setVerificationCode(librarianDto.getVerificationCode());
        librarian.setVerificationCodeTime(librarianDto.getVerificationCodeTime());
        return librarian;
    }

    public LibrarianDto mapLibrarianDtoFromLibrarian(Librarian librarian) {
        LibrarianDto librarianDto = new LibrarianDto();
        librarianDto.setId(librarian.getId());
        librarianDto.setName(librarian.getName());
        librarianDto.setEmail(librarian.getEmail());
        librarianDto.setPassword(librarian.getPassword());
        librarianDto.setLibrary(libraryMapper.mapLibraryDtoFromLibrary(librarian.getLibrary()));
        librarianDto.setVerifiedAccount(librarian.isVerifiedAccount());
        librarianDto.setVerificationCode(librarian.getVerificationCode());
        librarianDto.setVerificationCodeTime(librarian.getVerificationCodeTime());
        return librarianDto;
    }

    public List<Librarian> mapLibrarianListFromLibrarianDtoList(List<LibrarianDto> librarianDtoList) {
        return librarianDtoList.stream()
                .map(this::mapLibrarianFromLibrarianDto)
                .toList();
    }

    public List<LibrarianDto> mapLibrarianDtoListFromLibrarianList(List<Librarian> librarianList) {
        return librarianList.stream()
                .map(this::mapLibrarianDtoFromLibrarian)
                .toList();
    }
}
