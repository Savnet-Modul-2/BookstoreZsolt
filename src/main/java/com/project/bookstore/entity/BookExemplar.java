package com.project.bookstore.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "book_exemplar")
@Table(name = "book_exemplar", schema = "public")
public class BookExemplar {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "publisher")
    private String publisher;
    @Column(name = "maximumReservationDuration")
    private int maximumReservationDuration;
    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            mappedBy = "reservedExemplar")
    private List<Reservation> reservations = new ArrayList<>();
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

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
