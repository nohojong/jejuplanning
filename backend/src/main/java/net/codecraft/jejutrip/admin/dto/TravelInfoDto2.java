package net.codecraft.jejutrip.admin.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelInfoDto2{
	private String SPOT_ID;
	private String ALL_TAG;
	private String CONTENTS_ID;
	private String CONTENTS_CD_LABEL;
	private String TITLE;
	private String REGION1_CD;
	private String REGION2_CD;
	private String ADDRESS;
	private String ROAD_ADDRESS;
	private String TAG;
	private String INTRODUCTION;
	private String LATITUDE;
	private String LONGITUDE;
	private String POST_CODE;
	private String PHONE_NO;
	private String IMGPATH;
	private String THUMBNAILPATH;
	private String ORG_NAME;
	private String TRAVEL_AREA;
	private LocalDateTime REG_DT;
	private LocalDateTime MOD_DT;
}