package com.project.bookstore.controller;

import com.project.bookstore.dto.ReviewsDto;
import com.project.bookstore.entity.Reviews;
import com.project.bookstore.mapper.ReviewsMapper;
import com.project.bookstore.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewsMapper reviewsMapper;

    @PostMapping("/{bookId}/{userId}")
    public ResponseEntity<?> addReviewToABook(@PathVariable(name = "bookId") Long bookId,
                                              @PathVariable(name = "userId") Long userId,
                                              @RequestBody ReviewsDto reviewsDto) {
        Reviews reviews = reviewService.addReviewToABook(bookId, userId, reviewsMapper.mapReviewsFromReviewsDto(reviewsDto));
        return ResponseEntity.ok(reviewsMapper.mapReviewsDtoFromReviews(reviews));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getReviewsForABook(@PathVariable(name = "bookId", required = false) Long bookId,
                                                @RequestParam(name = "pageSize", required = false) Integer pageSize,
                                                @RequestParam(name = "pageNumber") Integer pageNumber,
                                                @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
                                                @RequestParam(name = "sortBy") String sortBy) {
        if (pageSize != null || pageNumber != null) {
            Page<Reviews> pageReviews = reviewService.findReviewsForABook(bookId, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDir), sortBy)));
            return ResponseEntity.ok(pageReviews.map(reviews -> reviewsMapper.mapReviewsDtoFromReviews(reviews)));
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable(name = "reviewId") Long reviewId,
                                          @RequestBody ReviewsDto reviewsDto) {
        Reviews updatedReview = reviewService.updateReview(reviewId, reviewsMapper.mapReviewsFromReviewsDto(reviewsDto));
        return ResponseEntity.ok(reviewsMapper.mapReviewsDtoFromReviews(updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
