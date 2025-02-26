package com.project.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BookExemplarDto {
    private Long id;
    private String publisher;
    private int maximumReservationDuration;
    @JsonIgnore
    private BookDto book;
    private int nrOfExemplarsToCreate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getMaximumReservationDuration() {
        return maximumReservationDuration;
    }

    public void setMaximumReservationDuration(int maximumReservationDuration) {
        this.maximumReservationDuration = maximumReservationDuration;
    }

    public BookDto getBook() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book = book;
    }

    public int getNrOfExemplarsToCreate() {
        return nrOfExemplarsToCreate;
    }

    public void setNrOfExemplarsToCreate(int nrOfExemplarsToCreate) {
        this.nrOfExemplarsToCreate = nrOfExemplarsToCreate;
    }
}
