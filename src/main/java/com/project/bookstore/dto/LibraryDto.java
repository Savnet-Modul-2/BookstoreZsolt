package com.project.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

//TODO: resolve LibraryDto to show parent(LibrarianDto)
public class LibraryDto {
    private Long id;
    private String name;
    private String city;
    private String phoneNumber;
    @JsonIgnore
    private LibrarianDto librarian;
    //Am pus variabila asta in DTO, respectiv si in Mapper doar ca sa verific ca s-a salvat In DB
    private List<String> userNames = new ArrayList<>();

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LibrarianDto getLibrarian() {
        return librarian;
    }

    public void setLibrarian(LibrarianDto librarian) {
        this.librarian = librarian;
    }


}
