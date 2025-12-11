package net.codecraft.jejutrip.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
public class FacilitesInfoDto {

    private String FACILITY_ID;
    private double SEQ;
    private String TRAVEL_ID;
    private String TRIP_PLACE_TYPE;
    private String FACILITY_NAME;
    private String FACILITY_SEPARATE;
    private String FACILITY_TYPE;    
    private String ADDRESS;    
    private String TEL;    
    private LocalDateTime OP_TIME;
    private BigDecimal LATITUDE;
    private BigDecimal LONGITUDE;
    private LocalDateTime REG_DT;
    private LocalDateTime MOD_DT;
}
