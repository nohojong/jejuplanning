package net.codecraft.jejutrip.admin.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import net.codecraft.jejutrip.admin.dto.BestRestaurantInfoDto;
import net.codecraft.jejutrip.admin.dto.FacilitesInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto2;
import net.codecraft.jejutrip.admin.entity.BestRestaurantInfo;
import net.codecraft.jejutrip.admin.entity.FacilitiesInfo;
import net.codecraft.jejutrip.admin.entity.TravelAreaInfo;
import net.codecraft.jejutrip.admin.entity.TravelInfo;
import net.codecraft.jejutrip.admin.entity.TravelInfo2;
import net.codecraft.jejutrip.common.Util.LocalDateTimeConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.*;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

@Repository
public class JsonToTableRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public LocalDateTimeConverter getLaocalDateTimeConvert() {
		return laocalDateTimeConvert;
	}

	public void setLaocalDateTimeConvert(LocalDateTimeConverter laocalDateTimeConvert) {
		this.laocalDateTimeConvert = laocalDateTimeConvert;
	}

	public LocalDateTimeConverter laocalDateTimeConvert;

	@Transactional
	public void saveCsvToDb(@RequestBody TravelInfo dto) throws ArrayIndexOutOfBoundsException, URISyntaxException, IOException, InterruptedException, DateTimeParseException {
		TravelInfo entity = new TravelInfo();
		int j = 0;

		try {
			String filePath = "C:\\제주관광공사_제주관광정보시스템(VISIT JEJU)_여행장소_20250307.CSV"; // Reader 생성

			Reader reader = new FileReader(filePath);
			CsvToBean<TravelInfo> csvToBean = new CsvToBeanBuilder<TravelInfo>(reader)
					.withType(TravelInfo.class)
					.withIgnoreLeadingWhiteSpace(true)
					.build();

			//TravelInfo travelInfo = new TravelInfo();
			List<TravelInfo> travelList = new ArrayList<>();
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = br.readLine()) != null) {

				line = line.substring(0, line.toString().length() - 2);

				String [] arrTravelInfo = line.split(",");

				TravelInfo info = new TravelInfo();

				info.setSPOT_NANE(arrTravelInfo[0]);
				info.setCIT_ADDRESS(arrTravelInfo[1]);
				info.setLATITUDE(arrTravelInfo[2]);
				info.setLONGITUDE(arrTravelInfo[3]);

				//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				//LocalDateTimeConverter ldate = new LocalDateTimeConverter();

				info.setREG_DT(arrTravelInfo[4]);
				info.setMOD_DT(arrTravelInfo[5]);
				info.setJIBUN_ADDRESS(arrTravelInfo[6]);

				travelList.add(info);
			}

			for (TravelInfo info : travelList) {
				//entity.setSPOT_ID(travelInfo.getSPOT_ID());
				entity.setSPOT_NANE(info.getSPOT_NANE());
				entity.setCIT_ADDRESS(info.getCIT_ADDRESS());
				entity.setLATITUDE(info.getLATITUDE());
				entity.setLONGITUDE(info.getLONGITUDE());
				entity.setJIBUN_ADDRESS(info.getJIBUN_ADDRESS());
				entity.setUSE_YN(info.getUSE_YN());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				entity.setREG_DT(info.getREG_DT());
				entity.setMOD_DT(info.getMOD_DT());
				entity.setUSE_YN(info.getUSE_YN());

				if (info.getSPOT_ID() == null) {
					entityManager.persist(info); // 신규 엔티티일 때만 persist
				} else {
					entityManager.merge(info);   // 기존 ID가 있다면 merge
				}

				entityManager.flush(); // DB에 바로 insert 쿼리 실행
			}

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			throw new RuntimeException("CSV 처리 중 오류 발생 :::: CSV 처리 실패", e);
		}
	}

	@Transactional
	public void saveDtoToDb2(@RequestBody TravelInfoDto2 dto) throws URISyntaxException, IOException, InterruptedException {
		TravelInfo2 entity = new TravelInfo2();

		entity.setSPOT_ID(dto.getSPOT_ID());
		entity.setALL_TAG(dto.getALL_TAG());
		entity.setCONTENTS_ID(dto.getCONTENTS_ID());
		entity.setCONTENTS_CD_LABEL(dto.getCONTENTS_CD_LABEL());
		entity.setTITLE(dto.getTITLE());
		entity.setLATITUDE(dto.getLATITUDE());
		entity.setLONGITUDE(dto.getLONGITUDE());
		entity.setREGION1_CD(dto.getREGION1_CD());
		entity.setREGION2_CD(dto.getREGION2_CD());
		entity.setADDRESS(dto.getADDRESS());
		entity.setROAD_ADDRESS(dto.getROAD_ADDRESS());
		entity.setTAG(dto.getTAG());
		entity.setINTRODUCTION(dto.getINTRODUCTION());
		entity.setLATITUDE(dto.getLATITUDE());
		entity.setLONGITUDE(dto.getLONGITUDE());
		entity.setPOST_CODE(dto.getPOST_CODE());
		entity.setPHONE_NO(dto.getPHONE_NO());
		entity.setIMGPATH(dto.getIMGPATH());
		entity.setTHUMBNAILPATH(dto.getTHUMBNAILPATH());

		entity.setREG_DT(dto.getREG_DT());
		entity.setMOD_DT(dto.getMOD_DT());

		if (entity.getSPOT_ID() == null) {
			entityManager.persist(entity); // 신규 엔티티일 때만 persist
		} else {
			entityManager.merge(entity);   // 기존 ID가 있다면 merge
		}

		entityManager.flush(); // DB에 바로 insert 쿼리 실행
	}

	@Transactional
	public void saveTravelAreaInfo(TravelInfoDto2 dto) throws Exception {
		TravelAreaInfo entity = new TravelAreaInfo();

		entity.setTRAVEL_ID(dto.getSPOT_ID());

		if (entity.getTRAVEL_ID() == null) {
			//여행지 마스터 테이블
			String strSql =  """
    		      INSERT INTO TRAVEL_AREA_INFO ( 
    		    		 TRAVEL_ID
    		    		 ,AREA_NAME
    		    		 ,TRAVEL_TYPE
    		    		 ,TRAVEL_SPOT
    		    		 ,TRAVEL_SEPARATE        
    		    		 ,CAPACITY_CNT
    		    		 ,TEL
    		    		 ,ORG_NAME
    		    		 ,TRAVEL_AREA
    		    		 ,REG_DT
    		    		 )
    		      SELECT a.spot_id,
    			         a.title as area_name,
    			        'J' as travel_type,  
    			        '1' as travel_spot,
    			        '1' as TRAVEL_SEPARATE,    
    			          0 as CAPACITY_CNT,
    			         a.phone_no as tel,
    			         CONCAT(a.region1_cd, ' ', a.region2_cd) as org_name,
    			         '',
    			         a.reg_dt              
    				FROM jejutrip.travel_info2 a
    				left join jejutrip.travel_fac c
    		        on a.latitude = c.latitude and a.longitude = c.longitude and a.address = c.road_address
            """;

			entityManager.createNativeQuery(strSql).executeUpdate();

			System.out.println(" 여행지마스터 테이블 생성완료 !!! ");

			saveTravelAreaInfo(dto);

			entityManager.persist(entity); // 신규 엔티티일 때만 persist
		} else {
			entityManager.merge(entity);   // 기존 ID가 있다면 merge
		}

		entityManager.flush();
	}

	@Transactional
	public void saveFacilitesInfo(FacilitesInfoDto dto) throws Exception {
		FacilitiesInfo entity = new FacilitiesInfo();

		String strSql =  """
	            INSERT INTO FACILITIES_INFO (
	                FACILITY_ID,
	                SEQ,
	                TRAVEL_ID,
	                FACILITY_NAME,
	                FACILITY_SEPARATE,
	                FACILITY_TYPE,
	                ADDRESS,
	                TEL,
	                OP_TIME,
	                LATITUDE,
	                LONGITUDE,
	                REG_DT,
	                MOD_DT
	            )
	            SELECT 
	                CONCAT('FAC_', ROW_NUMBER() OVER (ORDER BY B.SPOT_ID)) AS FACILITY_ID,
	                ROW_NUMBER() OVER (PARTITION BY A.FACILITY_NAME ORDER BY A.FACILITY_NAME) AS SEQ,
	                ANY_VALUE(B.SPOT_ID) AS TRAVEL_ID,
	                ANY_VALUE(A.FACILITY_NAME),
	                '' AS FACILITY_SEPARATE,
	                ANY_VALUE(A.FACILITY_TYPE),
	                ANY_VALUE(B.ROAD_ADDRESS),
	                ANY_VALUE(A.TEL),
	                ANY_VALUE(A.OP_TIME),
	                ANY_VALUE(B.LATITUDE),
	                ANY_VALUE(B.LONGITUDE),
	                NOW() AS REG_DT,
	                CURRENT_TIMESTAMP AS MOD_DT
	            FROM jejutrip.travel_fac A
	            LEFT JOIN jejutrip.travel_info2 B
	                ON A.road_address = B.address
	               AND A.latitude = B.latitude
	               AND A.longitude = B.longitude
	            WHERE NOT EXISTS (
	                SELECT 1 FROM FACILITIES_INFO FI
	                WHERE FI.TRAVEL_ID = B.SPOT_ID AND FI.FACILITY_NAME = A.FACILITY_NAME
	            )
	            GROUP BY B.SPOT_ID, A.FACILITY_NAME
	            """;

		entityManager.createNativeQuery(strSql).executeUpdate();

		if (entity.getFACILITY_ID() != null) {
			entityManager.merge(entity);     // 기존 엔티티일 때만 persist
		} else {
			entityManager.persist(entity);   // 신규 ID가 있다면 merge
		}

		entityManager.flush();
	}

	@Transactional
	public void saveBestRestaurantInfo(BestRestaurantInfoDto dto) throws Exception {
		BestRestaurantInfo entity = new BestRestaurantInfo();

		String strSql =  """
		            INSERT INTO FACILITIES_INFO (
		                FOOD_ID,
						FACILITY_ID,
						TRAVEL_ID,
						SEQ,
						RESTAURANT_TYPE,
						BEST_MENU
					)		            )
		            SELECT 
		                CONCAT('FOO_', ROW_NUMBER() OVER (ORDER BY `SPOT_ID)) AS FACILITY_ID,
		                ROW_NUMBER() OVER (PARTITION BY A.FACILITY_NAME ORDER BY A.FACILITY_NAME) AS SEQ,
		                ANY_VALUE(B.SPOT_ID) AS TRAVEL_ID,
		                ANY_VALUE(A.FACILITY_NAME),
		                '' AS FACILITY_SEPARATE,
		                ANY_VALUE(A.FACILITY_TYPE),
		                ANY_VALUE(B.ROAD_ADDRESS),
		                ANY_VALUE(A.TEL),
		                ANY_VALUE(A.OP_TIME),
		                ANY_VALUE(B.LATITUDE),
		                ANY_VALUE(B.LONGITUDE),
		                NOW() AS REG_DT,
		                CURRENT_TIMESTAMP AS MOD_DT
		            FROM jejutrip.travel_fac A
		            LEFT JOIN jejutrip.BEST_RESTAURANT_INFO B
		                ON A.road_address = B.address
		               AND A.latitude = B.latitude
		               AND A.longitude = B.longitude
		            WHERE NOT EXISTS (
		                SELECT 1 FROM FACILITIES_INFO FI
		                WHERE FI.TRAVEL_ID = B.SPOT_ID AND FI.FACILITY_NAME = A.FACILITY_NAME
		            )
		            GROUP BY B.SPOT_ID, A.FACILITY_NAME
		            """;

		entityManager.createNativeQuery(strSql).executeUpdate();

		if (entity.getFACILITY_ID() != null) {
			entityManager.merge(entity);     // 기존 엔티티일 때만 persist
		} else {
			entityManager.persist(entity);   // 신규 ID가 있다면 merge
		}

		entityManager.flush();
	}

}