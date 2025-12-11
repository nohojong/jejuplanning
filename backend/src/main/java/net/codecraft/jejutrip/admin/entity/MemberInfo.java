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
@Table(name = "MemberInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", nullable = false, updatable = false)
	private String USER_ID;
	 
	@Column(name = "USER_NAME")
	private String USER_NAME;
	
	@Column(name = "AGREE_YN")
	private String AGREE_YN;
	
    @Column(name ="AUTH_YN") 
	private String AUTH_YN;
	
	@Column(name = "EMAIL_ADDR")
	private String EMAIL_ADDR;
	
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

