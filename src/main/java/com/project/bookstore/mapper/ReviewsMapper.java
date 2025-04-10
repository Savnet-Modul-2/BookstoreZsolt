package com.project.bookstore.mapper;

import com.project.bookstore.dto.ReviewsDto;
import com.project.bookstore.entity.Reviews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewsMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;


    public Reviews mapReviewsFromReviewsDto(ReviewsDto reviewsDto) {
        Reviews reviews = new Reviews();
        reviews.setReviewNote(reviewsDto.getReviewNote());
        reviews.setDescription(reviewsDto.getDescription());
        return reviews;
    }

    public ReviewsDto mapReviewsDtoFromReviews(Reviews reviews) {
        ReviewsDto reviewsDto = new ReviewsDto();
        reviewsDto.setId(reviews.getId());
        reviewsDto.setReviewDate(reviews.getReviewDate());
        reviewsDto.setReviewNote(reviews.getReviewNote());
        reviewsDto.setDescription(reviews.getDescription());
        reviewsDto.setReviewerUser(userMapper.mapUserDtoFromUser(reviews.getReviewerUser()));
        reviewsDto.setReviewedBook(bookMapper.mapBookDtoFromBook(reviews.getReviewedBook()));
        return reviewsDto;
    }


}
