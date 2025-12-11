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
@Table(name = "Codebase")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Codebase {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BCODE", nullable = false, updatable = false)
	private String BCODE;
	 
	@Column(name = "MCODE")
	private String MCODE;
	
	@Column(name = "SCODE")
	private String SCODE;
	
	@Column(name = "CD_NAME")
	private String CD_NAME;
	
	@Column(name = "REMARK")
	private String REMARK;
	
	@Column(name = "PASSWORD_INIT_DATE")
	private LocalDateTime PASSWORD_INIT_DATE;
	
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;
	
    @PrePersist
    public void prePersist() {
        if (this.BCODE == null) {
            this.BCODE = UUID.randomUUID().toString();
        }
    }
}

