package net.codecraft.jejutrip.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "BestRestaurantInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BestRestaurantInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BEST_RES_ID", nullable = false, updatable = false)
    private String BEST_RES_ID;
    
    @Column(name = "FACILITY_ID")
    private String FACILITY_ID;
    
    @Column(name = "TRAVEL_ID")
    private String TRAVEL_ID;
    
    @Column(name = "SEQ")
    private double SEQ;
    
    @Column(name = "RESTAURANT_TYPE")
    private String RESTAURANT_TYPE;
    
    @Column(name = "BEST_MENU")
    private String BEST_MENU;  
    
    @Column(name = "TEL")
    private String TEL; 
    
    @PrePersist
    public void prePersist() {
        if (this.BEST_RES_ID == null) {
            this.BEST_RES_ID = UUID.randomUUID().toString();
        }
    }
}
