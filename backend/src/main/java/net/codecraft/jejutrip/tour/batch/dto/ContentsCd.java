package net.codecraft.jejutrip.tour.batch.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentsCd {
    private String value;
    private String label;
    private String refId;
}