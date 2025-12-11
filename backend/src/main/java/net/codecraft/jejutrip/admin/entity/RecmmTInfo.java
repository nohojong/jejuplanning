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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "RecmmTInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecmmTInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRAVEL_ID", nullable = false, updatable = false)
	private String TRAVEL_ID;
	 
	@Column(name = "CONTENTS")
	private String CONTENTS;
	
    @Column(name ="TRAVEL_GUIDE") 
	private String TRAVEL_GUIDE;
	
	@Column(name = "POPLUAR_CNT")
	private double POPLUAR_CNT;
		
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



