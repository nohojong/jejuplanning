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
@Table(name = "UserInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false, updatable = false)
	private String USER_ID;
	
	@Column(name = "USER_NAME")
	private String USER_NAME;
	 
	@Column(name = "PASSWORD")
	private String PASSWORD;
	
	@Column(name = "ADDRESS1")
	private String ADDRESS1;
	
	@Column(name = "ADDRESS2")
	private String ADDRESS2;
	
	@Column(name = "TEL")
	private String TEL;
	
	@Column(name = "ERR")
	private String ERR;
	
	@Column(name = "PASSWORD_INIT_DATE")
	private LocalDateTime PASSWORD_INIT_DATE;
	
	@Column(name = "AUTH_ID")
	private String AUTH_ID;
	
	@Column(name = "ROLE_ID")
	private String ROLE_ID;
	
	@Column(name = "DEL_YN")
	private String DEL_YN;
	
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;
	
	
	@PrePersist
    public void prePersist() {
        if (this.USER_ID == null) {
            this.USER_ID = UUID.randomUUID().toString();
        }
    }

}
