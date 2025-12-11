package net.codecraft.jejutrip.tour.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.exception.UserNotFoundException;
import net.codecraft.jejutrip.account.user.repository.UserRepository;
import net.codecraft.jejutrip.tour.place.domain.Place;
import net.codecraft.jejutrip.tour.place.exception.PlaceNotFoundException;
import net.codecraft.jejutrip.tour.place.repository.PlaceRepository;
import net.codecraft.jejutrip.tour.review.domain.Review;
import net.codecraft.jejutrip.tour.review.dto.ReviewRequest;
import net.codecraft.jejutrip.tour.review.dto.ReviewResponse;
import net.codecraft.jejutrip.tour.review.exception.ReviewException;
import net.codecraft.jejutrip.tour.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public ReviewResponse createReview(Long placeId, ReviewRequest request, String email) {
        User reviewer = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다. email=" + email));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("해당 장소를 찾을 수 없습니다. id=" + placeId));

        Review review = Review.builder()
                .reviewer(reviewer)
                .place(place)
                .rating(request.getRating())
                .content(request.getContent())
                .build();

        reviewRepository.save(review);
        return new ReviewResponse(review);
    }

    @Transactional
    public ReviewResponse updateReview(Long placeId, Long reviewId, ReviewRequest request, String email) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException("리뷰를 찾을 수 없습니다. id=" + reviewId));
        
        if (!review.getReviewer().getEmail().equals(email)) {
            log.warn("Review update unauthorized. ReviewId: {}, UserEmail: {}", reviewId, email);
            throw new ReviewException("리뷰를 수정할 권한이 없습니다.");
        }
        if (!review.getPlace().getId().equals(placeId)) {
            log.warn("Review place mismatch. ReviewId: {}, PlaceId: {}", reviewId, placeId);
            throw new ReviewException("URL의 장소 정보가 일치하지 않습니다.");
        }

        review.update(request.getRating(), request.getContent());
        return new ReviewResponse(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long placeId, String email) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException("리뷰를 찾을 수 없습니다. id=" + reviewId));
        
        if (!review.getReviewer().getEmail().equals(email)) {
            log.warn("Review delete unauthorized. ReviewId: {}, UserEmail: {}", reviewId, email);
            throw new ReviewException("리뷰를 삭제할 권한이 없습니다.");
        }
        if (!review.getPlace().getId().equals(placeId)) {
            log.warn("Review place mismatch. ReviewId: {}, PlaceId: {}", reviewId, placeId);
            throw new ReviewException("URL의 장소 정보가 일치하지 않습니다.");
        }

        reviewRepository.delete(review);
        log.info("Review deleted. ReviewId: {}, PlaceId: {}", reviewId, placeId);
    }


}