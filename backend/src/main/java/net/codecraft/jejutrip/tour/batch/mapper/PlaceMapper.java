package net.codecraft.jejutrip.tour.batch.mapper;

import net.codecraft.jejutrip.tour.batch.dto.VisitJejuItem;
import net.codecraft.jejutrip.tour.place.domain.Place;
import net.codecraft.jejutrip.tour.place.domain.PlaceType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class PlaceMapper {

    public Place toEntity(VisitJejuItem dto) {
        if (dto == null) {
            return null;
        }

        String placeTypeValue = dto.getContentsCd() != null ? dto.getContentsCd().getValue() : null;
        PlaceType placeType = Arrays.stream(PlaceType.values())
                .filter(pt -> pt.getValue().equals(placeTypeValue))
                .findFirst()
                .orElse(null);

        return Place.builder()
                .contentsid(dto.getContentsid())
                .title(dto.getTitle())
                .introduction(dto.getIntroduction())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .allTag(dto.getAllTag())
                .placeType(placeType)
                .photoDescription(dto.getRepPhoto() != null ? dto.getRepPhoto().getDescseo() : null)
                .imgPath(dto.getRepPhoto() != null && dto.getRepPhoto().getPhotoid() != null ? dto.getRepPhoto().getPhotoid().getImgpath() : null)
                .thumbnailPath(dto.getRepPhoto() != null && dto.getRepPhoto().getPhotoid() != null ? dto.getRepPhoto().getPhotoid().getThumbnailpath() : null)
                .build();
    }

    public void updateEntityFromDto(Place place, VisitJejuItem dto) {

        if (dto == null || place == null) {
            return;
        }

        place.updateTitle(dto.getTitle());
        place.updateIntroduction(dto.getIntroduction());
        place.updateAddress(dto.getAddress());
        place.updatePhoneNumber(dto.getPhoneNumber());
        place.updateLatitude(dto.getLatitude());
        place.updateLongitude(dto.getLongitude());
        place.updateAllTag(dto.getAllTag());
        place.updatePhotoDescription(dto.getRepPhoto() != null ? dto.getRepPhoto().getDescseo() : null);
        place.updateImgPath(dto.getRepPhoto() != null && dto.getRepPhoto().getPhotoid() != null ? dto.getRepPhoto().getPhotoid().getImgpath() : null);
        place.updateThumbnailPath(dto.getRepPhoto() != null && dto.getRepPhoto().getPhotoid() != null ? dto.getRepPhoto().getPhotoid().getThumbnailpath() : null);
    }
}