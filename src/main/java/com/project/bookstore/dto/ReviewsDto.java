package com.project.bookstore.dto;

import com.project.bookstore.entity.types.ReviewNote;

import java.time.LocalDate;

public class ReviewsDto {
    private Long id;
    private ReviewNote reviewNote;
    private String description;
    private LocalDate reviewDate;
    private BookDto reviewedBook;
    private UserDto reviewerUser;

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

    public BookDto getReviewedBook() {
        return reviewedBook;
    }

    public void setReviewedBook(BookDto reviewedBook) {
        this.reviewedBook = reviewedBook;
    }

    public UserDto getReviewerUser() {
        return reviewerUser;
    }

    public void setReviewerUser(UserDto reviewerUser) {
        this.reviewerUser = reviewerUser;
    }
}
