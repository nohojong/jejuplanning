package net.codecraft.jejutrip.tour.place.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceType {
    ATTRACTION("c1"),
    SHOPPING("c2"),
    ACCOMMODATION("c3"),
    RESTAURANT("c4");

    private final String value;
}
