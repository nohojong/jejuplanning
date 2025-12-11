package net.codecraft.jejutrip.tour.place.repository;

import net.codecraft.jejutrip.tour.place.domain.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByContentsid(String contentsid);
    Page<Place> findByTitleContaining(String title, Pageable pageable);
}
