package net.codecraft.jejutrip.admin.dto;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

public class BestRestaurantInfoDto {
    private String BEST_RES_ID;
    private String FACILITY_ID;
    
    @Column(name = "TRAVEL_ID")
    private String TRAVEL_ID;
    
    @Column(name = "SEQ")
    private double SEQ;
    
    @Column(name = "RESTAURANT_TYPE")
    private String RESTAURANT_TYPE;
    
    @Column(name = "BEST_MENU")
    private String BEST_MENU;  
    
    @PrePersist
    public void prePersist() {
        if (this.BEST_RES_ID == null) {
            this.BEST_RES_ID = UUID.randomUUID().toString();
        }
    }
}
