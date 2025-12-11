package net.codecraft.jejutrip.admin.service;

import jakarta.persistence.*;
import net.codecraft.jejutrip.admin.dto.BestRestaurantInfoDto;
import net.codecraft.jejutrip.admin.dto.FacilitesInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelAreaInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto;
import net.codecraft.jejutrip.admin.dto.TravelInfoDto2;
import net.codecraft.jejutrip.admin.entity.TravelInfo;
import net.codecraft.jejutrip.admin.repository.JsonToTableRepository;
import net.codecraft.jejutrip.admin.repository.JsonToTableRepositoryImpl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ComponentScan
public class JsonToTableService {
    @Autowired
    private final JsonToTableRepositoryImpl repository;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public JsonToTableService(JsonToTableRepositoryImpl repository) {
        this.repository = repository;
    }

    public void saveDto(TravelInfo travelInfo) throws URISyntaxException, IOException, InterruptedException {
        this.repository.saveCsvToDb(travelInfo);
    }
    
    public void saveDto2(TravelInfoDto2 travelDto) throws URISyntaxException, IOException, InterruptedException {
        this.repository.saveDtoToDb2(travelDto);
    }
    
    //여행지 마스터 테이블에 값 넣기
    public void saveTravelAreaInfo(TravelInfoDto2 dto) throws Exception{    	
    	this.repository.saveTravelAreaInfo(dto);
    }
    
    //편의시설 테이블에 값 넣기
    public void saveFacilitesInfo(FacilitesInfoDto dto) throws Exception{
    	this.repository.saveFacilitesInfo(dto);    	
    }
    
    //맛집
    public void saveBestRestaurantInfo(BestRestaurantInfoDto dto) throws Exception{
    	this.repository.saveBestRestaurantInfo(dto);
    }
}
