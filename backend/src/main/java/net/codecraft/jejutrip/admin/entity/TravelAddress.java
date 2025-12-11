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
@Table(name = "TravelAddress")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TravelAddress {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAVEL_ID", nullable = false, updatable = false)
    private String TRAVEL_ID;
    
    @Column(name = "ADDRESS1")
    private String ADDRESS1;
    
    @Column(name = "ADDRESS2")
    private double ADDRESS2;
    
    @Column(name = "REG_DT")
    private LocalDateTime REG_DT;
    
    @PrePersist
    public void prePersist() {
        if (this.TRAVEL_ID == null) {
            this.TRAVEL_ID = UUID.randomUUID().toString();
        }
    }
   
}

