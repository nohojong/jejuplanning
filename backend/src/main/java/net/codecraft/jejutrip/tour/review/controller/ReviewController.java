package net.codecraft.jejutrip.tour.review.controller;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.tour.review.domain.Review;
import net.codecraft.jejutrip.tour.review.dto.ReviewRequest;
import net.codecraft.jejutrip.tour.review.dto.ReviewResponse;
import net.codecraft.jejutrip.tour.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/places/{placeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 특정 장소에 리뷰 작성
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long placeId,
                                                       @RequestBody ReviewRequest request,
                                                       @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = userDetails.getUsername();
        ReviewResponse newReview = reviewService.createReview(placeId, request, email);

        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getUsername();

        ReviewResponse updatedReview = reviewService.updateReview(placeId, reviewId, request, email);

        return ResponseEntity.status(HttpStatus.OK).body(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long placeId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = userDetails.getUsername();
        reviewService.deleteReview(placeId, reviewId, email);

        return ResponseEntity.noContent().build();
    }
}