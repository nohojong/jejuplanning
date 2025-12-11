package net.codecraft.jejutrip.tour.batch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class PhotoId {
    private String imgpath;
    private String thumbnailpath;
}
