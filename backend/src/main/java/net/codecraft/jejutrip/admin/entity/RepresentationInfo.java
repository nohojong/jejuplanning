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
@Table(name = "RepresentationInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RepresentationInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REPRESENT_IMAGE_ID", nullable = false, updatable = false)
	private String REPRESENT_IMAGE_ID;
	 
	@Column(name = "THUMNAIL_PATH")
	private String THUMNAIL_PATH;
	
	@Column(name = "THUMNAIL_NM")
	private String THUMNAIL_NM;
	
    @Column(name ="REPRESENT_IMAGE_PATH") 
	private String REPRESENT_IMAGE_PATH;
	
	@Column(name = "TRAVEL_ID")
	private String TRAVEL_ID;
	
	@Column(name = "REPRESENT_IMAGE_NM")
	private LocalDateTime REPRESENT_IMAGE_NM;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;	
	
    @PrePersist
    public void prePersist() {
        if (this.REPRESENT_IMAGE_ID == null) {
            this.REPRESENT_IMAGE_ID = UUID.randomUUID().toString();
        }
    }
}
