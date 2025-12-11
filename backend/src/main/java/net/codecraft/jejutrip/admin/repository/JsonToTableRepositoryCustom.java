package net.codecraft.jejutrip.admin.repository;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.transaction.Transactional;
import net.codecraft.jejutrip.admin.dto.FacilitesInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto2;
import net.codecraft.jejutrip.admin.entity.TravelInfo;
import net.codecraft.jejutrip.admin.entity.TravelInfo2;

public interface JsonToTableRepositoryCustom {
	
	public void saveCsvToDb(TravelInfo travelInfo) throws URISyntaxException, IOException, InterruptedException;
	
	public void saveDtoToDb2(TravelInfoDto2 dto) throws URISyntaxException, IOException, InterruptedException;
	
	public void saveTravelAreaInfo(@RequestBody TravelInfoDto2 dto) throws Exception;
	
	public void saveFacilitesInfo(@RequestBody FacilitesInfoDto dto) throws Exception;
}
