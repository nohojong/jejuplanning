package net.codecraft.jejutrip.tour.place.service;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.tour.place.domain.Place;
import net.codecraft.jejutrip.tour.place.dto.PlaceResponse;
import net.codecraft.jejutrip.tour.place.repository.PlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;

    /**
     * 모든 장소 목록을 페이징하여 조회합니다.
     * 예시: /api/places?page=0&size=10 -> 0번 페이지의 10개 데이터를 요청
     */
    public Page<PlaceResponse> findAllPlaces(Pageable pageable) {
        Page<Place> placePage = placeRepository.findAll(pageable);
        return placePage.map(PlaceResponse::new);
    }

    // ID로 특정 장소 조회
    public PlaceResponse findPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 장소를 찾을 수 없습니다. id=" + id));
        return new PlaceResponse(place);
    }

    // 제목으로 장소 검색
    public Page<PlaceResponse> searchPlacesByTitle(String title, Pageable pageable) {

        Page<Place> placePage = placeRepository.findByTitleContaining(title, pageable);
        return placePage.map(PlaceResponse::new);
    }
}
