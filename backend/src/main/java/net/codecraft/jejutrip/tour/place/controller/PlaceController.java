package net.codecraft.jejutrip.tour.place.controller;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.tour.place.dto.PlaceResponse;
import net.codecraft.jejutrip.tour.place.service.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    // 모든 장소 목록 조회
    @GetMapping
    public ResponseEntity<Page<PlaceResponse>> getAllPlaces(Pageable pageable) {
        return ResponseEntity.ok(placeService.findAllPlaces(pageable));
    }

    // 특정 장소 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.findPlaceById(id));
    }

    // 장소 검색
    @GetMapping("/search")
    public ResponseEntity<Page<PlaceResponse>> searchPlaces(@RequestParam String title, Pageable pageable) {
        return ResponseEntity.ok(placeService.searchPlacesByTitle(title, pageable));
    }
}