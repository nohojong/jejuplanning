package net.codecraft.jejutrip.admin.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelInfoDto{
	private Long SPOT_ID;
	private String SPOT_NANE;
	private String CIT_ADDRESS;
	private String LATITUDE;	  //위도
	private String LONGITUDE;     //경도
	private String JIBUN_ADDRESS;
	private String USE_YN;
	private String REG_DT;
	private String MOD_DT;

}