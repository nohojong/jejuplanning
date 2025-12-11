package net.codecraft.jejutrip.admin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.opencsv.bean.CsvDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "FacilitiesInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FacilitiesInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FACILITY_ID", nullable = false, updatable = false)
    private String FACILITY_ID;
      
    @Column(name = "SEQ")
    private double SEQ;
    
    @Column(name = "TRAVEL_ID")
    private String TRAVEL_ID;
    
    @Column(name = "TRIP_PLACE_TYPE")
    private String TRIP_PLACE_TYPE;
    
    @Column(name = "FACILITY_NAME")
    private String FACILITY_NAME;
    
    @Column(name = "FACILITY_SEPARATE")
    private String FACILITY_SEPARATE;
    
    @Column(name = "FACILITY_TYPE")
    private String FACILITY_TYPE;    

    @Column(name = "ADDRESS")
    private String ADDRESS;    

    @Column(name = "TEL")
    private String TEL;    

    @Column(name = "OP_TIME")
    private LocalDateTime OP_TIME;
    
    @Column(name = "LATITUDE")
    private BigDecimal LATITUDE;
    
    @Column(name = "LONGITUDE")
    private BigDecimal LONGITUDE;
    
    @Column(name = "REG_DT")
    private LocalDateTime REG_DT;

    @Column(name = "MOD_DT")
    private LocalDateTime MOD_DT;
    
	@PrePersist
    public void prePersist() {
        if (this.FACILITY_ID == null) {
            this.FACILITY_ID = UUID.randomUUID().toString();
        }
    }
 
}
