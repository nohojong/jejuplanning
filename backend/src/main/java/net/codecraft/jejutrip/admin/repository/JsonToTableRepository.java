package net.codecraft.jejutrip.admin.repository;

import net.codecraft.jejutrip.admin.dto.FacilitesInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto2;
import net.codecraft.jejutrip.admin.entity.TravelAreaInfo;
import net.codecraft.jejutrip.admin.entity.TravelInfo;

import java.io.IOException;
import java.net.URISyntaxException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;


@Repository
public interface JsonToTableRepository extends JpaRepository<TravelAreaInfo, String> {

	public void saveCsvToDb(@RequestBody TravelInfo travelInfo) throws URISyntaxException, IOException, InterruptedException;

	public void saveDtoToDb2(@RequestBody TravelInfoDto2 dto) throws URISyntaxException, IOException, InterruptedException;

	//여행지 마스터 테이블에 값 넣기
	public void saveTravelAreaInfo(@RequestBody TravelInfoDto2 dto) throws Exception;

	//편의시설 테이블에 값 넣기
	public void saveFacilitesInfo(FacilitesInfoDto dto) throws Exception;

}