package net.codecraft.jejutrip.tour.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.tour.place.domain.Place;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    public void update(int rating, String content) {
        this.rating = rating;
        this.content = content;
    }

    @Builder
    public Review(User reviewer, Place place, int rating, String content) {
        this.reviewer = reviewer;
        this.place = place;
        this.rating = rating;
        this.content = content;
    }
}
