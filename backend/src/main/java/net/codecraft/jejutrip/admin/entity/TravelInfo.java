package net.codecraft.jejutrip.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TravelInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TravelInfo {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SPOT_ID")
	private String SPOT_ID;

	@Column(name = "SPOT_NANE")
	private String SPOT_NANE;

	@Column(name = "CIT_ADDRESS")
	private String CIT_ADDRESS;

	@Column(name = "LATITUDE")
	private String LATITUDE;

	@Column(name = "LONGITUDE")
	private String LONGITUDE;

	@Column(name = "JIBUN_ADDRESS")
	private String JIBUN_ADDRESS;

	@Column(name ="USE_YN")
	private String USE_YN;

	@CsvBindByName(column = "REG_DT")
	@Column(name = "REG_DT")
	private String REG_DT;

	@CsvBindByName(column = "MOD_DT")
	@Column(name = "MOD_DT")
	private String MOD_DT;


	@PrePersist
	public void prePersist() {
		if (this.SPOT_ID == null) {
			this.SPOT_ID = UUID.randomUUID().toString();
		}
	}
}