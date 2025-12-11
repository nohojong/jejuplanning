package net.codecraft.jejutrip.tour.batch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitJejuItem {

    private String contentsid;
    private String title;
    private String introduction;
    @JsonProperty("roadaddress")
    private String address;
    @JsonProperty("phoneno")
    private String phoneNumber;
    private double latitude;
    private double longitude;
    @JsonProperty("alltag")
    private String allTag;
    @JsonProperty("contentscd")
    private ContentsCd contentsCd;
    @JsonProperty("repPhoto")
    private RepPhoto repPhoto;
}
