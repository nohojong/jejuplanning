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
@Table(name = "TripArticleInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TripArticleInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRAVEL_ID", nullable = false, updatable = false)
	private String TRAVEL_ID;
	
	@Column(name = "TRIP_ARTICLE_ID")
	private String TRIP_ARTICLE_ID;
	 
	@Column(name = "TRIP_ARTICLE_NM")
	private String TRIP_ARTICLE_NM;
	
    @Column(name ="TRIP_ARTICLE_TYPE") 
	private String TRIP_ARTICLE_TYPE;
	
	@Column(name = "TRIP_ARTICLE_TITLE")
	private String TRIP_ARTICLE_TITLE;
	
	@Column(name = "TRIP_ARTICLE_CONTENTS")
	private String TRIP_ARTICLE_CONTENTS;
	
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;

	@PrePersist
    public void prePersist() {
        if (this.TRIP_ARTICLE_ID == null) {
            this.TRIP_ARTICLE_ID = UUID.randomUUID().toString();
        }
    }
	
}
