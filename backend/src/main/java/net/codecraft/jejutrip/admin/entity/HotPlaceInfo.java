package net.codecraft.jejutrip.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.opencsv.bean.CsvDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "HotPlaceInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotPlaceInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAVEL_ID", nullable = false, updatable = false)
    private String TRAVEL_ID;
    
    @Column(name = "HOT_PLACE_TYPE")
    private String HOT_PLACE_TYPE;
    
    @Column(name = "TRIP_PLACE_TYPE")
    private String TRIP_PLACE_TYPE;
    
    @Column(name = "REG_DT")
    private LocalDateTime REG_DT;
    
    @Column(name = "MOD_DT")
    private LocalDateTime MOD_DT;
    
    @PrePersist
    public void prePersist() {
        if (this.TRAVEL_ID == null) {
            this.TRAVEL_ID = UUID.randomUUID().toString();
        }
    }

    
}
