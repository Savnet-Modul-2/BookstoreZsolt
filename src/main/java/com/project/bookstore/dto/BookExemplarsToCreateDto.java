package com.project.bookstore.dto;

public class BookExemplarsToCreateDto {
    private int nrOfExemplarsToCreate;
    private String publisher;
    private int maximumReservationDuration;

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

    public int getNrOfExemplarsToCreate() {
        return nrOfExemplarsToCreate;
    }

    public void setNrOfExemplarsToCreate(int nrOfExemplarsToCreate) {
        this.nrOfExemplarsToCreate = nrOfExemplarsToCreate;
    }
}
