package net.codecraft.jejutrip.tour.review.repository;

import net.codecraft.jejutrip.tour.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
