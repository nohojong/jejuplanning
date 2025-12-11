package net.codecraft.jejutrip.tour.review.dto;

import lombok.Getter;
import net.codecraft.jejutrip.tour.review.domain.Review;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {
    private Long id;
    private int rating;
    private String content;
    private String reviewer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.reviewer = review.getReviewer().getEmail();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}