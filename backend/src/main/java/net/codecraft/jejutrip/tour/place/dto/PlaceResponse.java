package net.codecraft.jejutrip.tour.place.dto;

import lombok.Getter;
import net.codecraft.jejutrip.tour.place.domain.Place;
import net.codecraft.jejutrip.tour.place.domain.PlaceType;
import net.codecraft.jejutrip.tour.review.domain.Review;
import net.codecraft.jejutrip.tour.review.dto.ReviewResponse;

import java.util.List;

@Getter
public class PlaceResponse {
    private Long id;
    private String title;
    private PlaceType placeType;
    private String introduction;
    private String address;
    private String phoneNumber;
    private double latitude;
    private double longitude;
    private String allTag;
    private String imgPath;
    private String thumbnailPath;
    private List<ReviewResponse> reviews;

    public PlaceResponse(Place place) {
        this.id = place.getId();
        this.title = place.getTitle();
        this.placeType = place.getPlaceType();
        this.introduction = place.getIntroduction();
        this.address = place.getAddress();
        this.phoneNumber = place.getPhoneNumber();
        this.latitude = place.getLatitude();
        this.longitude = place.getLongitude();
        this.allTag = place.getAllTag();
        this.imgPath = place.getImgPath();
        this.thumbnailPath = place.getThumbnailPath();
        this.reviews = place.getReviews().stream()
                .map(review -> new ReviewResponse(review))
                .toList();
    }
}