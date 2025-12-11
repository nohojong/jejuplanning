package net.codecraft.jejutrip.admin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.CsvDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TravelAreaInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TravelAreaInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRAVEL_ID", nullable = false, updatable = false)
	private String TRAVEL_ID;
	 
	@Column(name = "AREA_NAME")
	private String AREA_NAME;
	
	@Column(name = "TRAVEL_TYPE")
	private String TRAVEL_TYPE;
	
    @Column(name ="TRAVEL_SPOT") 
	private String TRAVEL_SPOT;
	
	@Column(name = "TRAVEL_SEPARATE")
	private String TRAVEL_SEPARATE;
	
	@Column(name = "FACILITY_INFO")
	private String FACILITY_INFO;
	
	@Column(name = "CAPACITY_CNT")
	private BigDecimal CAPACITY_CNT;
	
	@Column(name = "TEL")
	private String TEL;
	
	@Column(name = "ORG_NAME")
	private String ORG_NAME;	
	
	@Column(name = "DATE_DT")
	private LocalDateTime DATE_DT;	
	
	@Column(name = "OP_TIME")
	private String OP_TIME;
	
	@Column(name = "TRAVEL_AREA")
	private String TRAVEL_AREA;
	
	@Column(name = "COMMENT_CNT")
	private BigDecimal COMMENT_CNT;
	
	@Column(name = "LIKE_CNT")
	private BigDecimal LIKE_CNT;
	
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "LATITUDE")
	private BigDecimal LATITUDE;
	
	@Column(name = "LONGITUDE")
	private BigDecimal LONGITUDE;
		
	@PrePersist
    public void prePersist() {
        if (this.TRAVEL_ID == null) {
            this.TRAVEL_ID = UUID.randomUUID().toString();
        }
    }
}