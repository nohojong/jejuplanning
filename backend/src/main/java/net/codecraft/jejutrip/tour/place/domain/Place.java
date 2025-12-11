package net.codecraft.jejutrip.tour.place.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.codecraft.jejutrip.tour.review.domain.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String contentsid;

    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Column(nullable = false)
    private String title;

    @Lob // 긴 텍스트를 위한 어노테이션
    @Column(columnDefinition = "TEXT")
    private String introduction;

    private String address;

    private String phoneNumber;

    private double latitude;

    private double longitude;

    @Lob // 긴 텍스트를 위한 어노테이션
    @Column(columnDefinition = "TEXT")
    private String allTag;

    private String photoDescription;

    private String imgPath;

    private String thumbnailPath;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    // Update fields
    public void updateTitle(String title) {this.title = title;}
    public void updateIntroduction(String introduction) {this.introduction = introduction;}
    public void updateAddress(String address) {this.address = address;}
    public void updatePhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void updateLatitude(double latitude) {this.latitude = latitude;}
    public void updateLongitude(double longitude) {this.longitude = longitude;}
    public void updateAllTag(String allTag) {this.allTag = allTag;}
    public void updatePhotoDescription(String photoDescription) {this.photoDescription = photoDescription;}
    public void updateImgPath(String imgPath) {this.imgPath = imgPath;}
    public void updateThumbnailPath(String thumbnailPath) {this.thumbnailPath = thumbnailPath;}

    @Builder
    public Place(String contentsid, PlaceType placeType, String title, String introduction, String address,
                 String phoneNumber, double latitude, double longitude, String allTag, String photoDescription,
                 String imgPath, String thumbnailPath) {
        this.contentsid = contentsid;
        this.placeType = placeType;
        this.title = title;
        this.introduction = introduction;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.allTag = allTag;
        this.photoDescription = photoDescription;
        this.imgPath = imgPath;
        this.thumbnailPath = thumbnailPath;
    }
}