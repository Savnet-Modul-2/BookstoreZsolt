package com.project.bookstore.entity;

import com.project.bookstore.entity.types.ReviewNote;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "review")
@Table(name = "reviews", schema = "public")
public class Reviews {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ReviewNote reviewNote;
    @Column(name = "description")
    private String description;
    @Column(name = "reviewDate")
    private LocalDate reviewDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private Book reviewedBook;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private User reviewerUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReviewNote getReviewNote() {
        return reviewNote;
    }

    public void setReviewNote(ReviewNote reviewNote) {
        this.reviewNote = reviewNote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Book getReviewedBook() {
        return reviewedBook;
    }

    public void setReviewedBook(Book reviewedBook) {
        this.reviewedBook = reviewedBook;
    }

    public User getReviewerUser() {
        return reviewerUser;
    }

    public void setReviewerUser(User reviewerUser) {
        this.reviewerUser = reviewerUser;
    }
}
