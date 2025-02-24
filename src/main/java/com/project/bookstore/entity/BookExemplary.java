package com.project.bookstore.entity;

import jakarta.persistence.*;

@Entity(name = "exemplary")
@Table(name = "exemplary", schema = "public")
public class BookExemplary {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "publisher")
    private String publisher;
    @Column(name = "maximumReservationDuration")
    private int maximumReservationDuration;
    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
