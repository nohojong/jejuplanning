package net.codecraft.jejutrip.tour.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.codecraft.jejutrip.tour.batch.client.VisitJejuApiClient;
import net.codecraft.jejutrip.tour.batch.dto.VisitJejuItem;
import net.codecraft.jejutrip.tour.place.domain.Place;
import net.codecraft.jejutrip.tour.place.repository.PlaceRepository;
import net.codecraft.jejutrip.tour.batch.mapper.PlaceMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDataSyncService {

    private final VisitJejuApiClient apiClient;
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    /**
     * DB에 존재하는 데이터는 업데이트하고, 존재하지 않는 데이터는 새로 생성합니다. (주기적인 동기화용)
     */

    @Async
    @Transactional
    public void synchronizePlaces() {
        log.info("Starting data synchronization (upsert) process...");

        // 1. API에서 모든 데이터를 중복 제거하여 가져옵니다.
        List<VisitJejuItem> allFetchedItems = apiClient.fetchAllItems();

        // 2. DB의 모든 기존 장소 데이터를 Map으로 변환하여 조회 속도를 높입니다. (DB 조회 1번)
        Map<String, Place> existingPlacesMap = placeRepository.findAll().stream()
                .collect(Collectors.toMap(Place::getContentsid, Function.identity()));

        log.info("Found {} existing places in the database.", existingPlacesMap.size());

        // 3. API 데이터와 DB 데이터를 비교하여 업데이트 또는 생성을 결정합니다.
        List<Place> placesToSave = new ArrayList<>();
        for (VisitJejuItem itemDto : allFetchedItems) {
            Place existingPlace = existingPlacesMap.get(itemDto.getContentsid());

            if (existingPlace != null) {
                // UPDATE: 기존 엔티티의 내용을 업데이트합니다.
                placeMapper.updateEntityFromDto(existingPlace, itemDto);
                placesToSave.add(existingPlace);
            } else {
                // INSERT: 새로운 엔티티를 생성합니다.
                placesToSave.add(placeMapper.toEntity(itemDto));
            }
        }

        // 4. 업데이트되거나 새로 생성된 모든 엔티티를 DB에 한번에 저장합니다.
        if (!placesToSave.isEmpty()) {
            placeRepository.saveAll(placesToSave);
            log.info("{} places have been synchronized (created or updated).", placesToSave.size());
        } else {
            log.info("No data to synchronize.");
        }
    }
}