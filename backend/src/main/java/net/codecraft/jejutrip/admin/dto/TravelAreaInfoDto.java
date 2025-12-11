package net.codecraft.jejutrip.admin.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
public class TravelAreaInfoDto {
	
	private String TRAVEL_ID;	
	private String AREA_NAME;		
	private String TRAVEL_TYPE;     
	private String TRAVEL_SPOT;	
	private String TRAVEL_SEPARATE;	
	private String FACILITY_INFO;
	private Double CAPACITY_CNT;	
	private String TEL;	
	private String ORG_NAME;		
	private LocalDateTime DATE_DT;		
	private String OP_TIME;	
	private String TRAVEL_AREA;	
	private Double COMMENT_CNT;	
	private Double LIKE_CNT;	
	private LocalDateTime REG_DT;
	
}