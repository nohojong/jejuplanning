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
@Table(name = "FileInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FILE_ID", nullable = false, updatable = false)
	private String FILE_ID;
	 
	@Column(name = "TRAVEL_ID")
	private double TRAVEL_ID;
	
    @Column(name ="FILE_PATH") 
	private String FILE_PATH;
	
	@Column(name = "ORGN_FILE_ID")
	private String ORGN_FILE_ID;
	
	@Column(name = "DEL_YN")
	private String DEL_YN;
	
	@Column(name = "FILE_NM")
	private String FILE_NM;
		
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;
	
	@PrePersist
    public void prePersist() {
        if (this.FILE_ID == null) {
            this.FILE_ID = UUID.randomUUID().toString();
        }
    }
}


