package net.codecraft.jejutrip.tour.batch.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitJejuApiResponse {
    private int pageCount;
    private List<VisitJejuItem> items;
}