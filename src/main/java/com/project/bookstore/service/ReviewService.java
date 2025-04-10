package com.project.bookstore.service;

import com.project.bookstore.entity.Book;
import com.project.bookstore.entity.Reviews;
import com.project.bookstore.entity.User;
import com.project.bookstore.repository.BookRepository;
import com.project.bookstore.repository.ReviewRepository;
import com.project.bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    public Reviews addReviewToABook(Long bookId, Long userId, Reviews reviews) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found".formatted(userId)));
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        foundUser.addReview(reviews);
        foundBook.addReview(reviews);
        foundBook.calculateAverage();
        reviews.setReviewDate(LocalDate.now());
        return reviewRepository.save(reviews);
    }

    public Page<Reviews> findReviewsForABook(Long bookId, PageRequest of) {
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        return new PageImpl<>(foundBook.getReviews(), of, foundBook.getReviews().size());
    }

    public Reviews updateReview(Long reviewId, Reviews reviews) {
        Reviews foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review with id %s not found".formatted(reviewId)));
        foundReview.setReviewNote(reviews.getReviewNote());
        foundReview.setDescription(reviews.getDescription());
        foundReview.getReviewedBook().calculateAverage();
        return reviewRepository.save(foundReview);
    }

    public void deleteReview(Long reviewId) {
        Reviews foundReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review with id %s not found".formatted(reviewId)));
        Book bookFromReview = foundReview.getReviewedBook();
        User userFromReview = foundReview.getReviewerUser();
        userFromReview.getUserReviews().remove(foundReview);
        bookFromReview.getReviews().remove(foundReview);
        bookFromReview.calculateAverage();
        bookRepository.save(bookFromReview);
        reviewRepository.deleteById(foundReview.getId());
    }
}
