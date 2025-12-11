package net.codecraft.jejutrip.tour.batch.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.tour.batch.service.PlaceDataSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin/places")
@RequiredArgsConstructor
public class AdminPlaceController {

    private final PlaceDataSyncService placeDataSyncService;

    /**
     * Visit Jeju API의 모든 데이터를 가져와 DB와 동기화합니다.
     * (기존 데이터는 업데이트, 새로운 데이터는 생성)
     * MANAGER 권한이 있는 사용자만 호출할 수 있습니다.
     */
    @PostMapping("/sync")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> synchronizePlaces() {
        log.info("API call received: Synchronize all places data. (Admin only)");

        placeDataSyncService.synchronizePlaces();

        return ResponseEntity.ok("장소 데이터 동기화를 시작합니다.");
    }
}
