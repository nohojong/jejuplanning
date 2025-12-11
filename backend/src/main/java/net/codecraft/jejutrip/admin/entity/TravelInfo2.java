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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TravelInfo2")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TravelInfo2 {
	@Id
	@Column(name = "SPOT_ID", nullable = false, updatable = false)
	private String SPOT_ID;	
	
	@Column(name = "ALL_TAG")
	private String ALL_TAG;
	
	@Column(name = "CONTENTS_ID")
	private String CONTENTS_ID;
	
	@Column(name = "CONTENTS_CD_LABEL")
	private String CONTENTS_CD_LABEL;
	
	@Column(name = "TITLE", columnDefinition = "TEXT")
	private String TITLE;
	
	@Column(name = "REGION1_CD")
	private String REGION1_CD;
	
	@Column(name = "REGION1_LABEL")
	private String REGION1_LABEL;
	
	@Column(name = "REGION2_CD")
	private String REGION2_CD;
	
	@Column(name = "REGION2_LABEL")
	private String REGION2_LABEL;
	
	@Column(name = "ADDRESS")
	private String ADDRESS; 
	
	@Column(name = "ROAD_ADDRESS")
	private String ROAD_ADDRESS;
	
	@Column(name = "TAG")
	private String TAG;
	
	@Column(name = "INTRODUCTION", columnDefinition = "LONGTEXT")
	private String INTRODUCTION;
	
	@Column(name = "LATITUDE")
	private String LATITUDE;
	
	@Column(name = "LONGITUDE")
	private String LONGITUDE;
	
	@Column(name = "POST_CODE")
	private String POST_CODE;
	
	@Column(name = "PHONE_NO")
	private String PHONE_NO;
	
	@Column(name = "IMGPATH", columnDefinition = "TEXT")
	private String IMGPATH;
	
	@Column(name = "THUMBNAILPATH", columnDefinition = "TEXT")
	private String THUMBNAILPATH;	
	
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;
	
	@Column(name = "ORG_NAME")
	private String ORG_NAME;
	
	@PrePersist
    public void prePersist() {
        if (this.SPOT_ID == null) {
            this.SPOT_ID = UUID.randomUUID().toString();
        }
    }
}

