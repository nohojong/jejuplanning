package net.codecraft.jejutrip.tour.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequest {
    private int rating;
    private String content;
}