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
@Table(name = "Event")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EVENT_ID", nullable = false, updatable = false)
	private String EVENT_ID;
	 
	@Column(name = "EVENT_TITLE")
	private String EVENT_TITLE;
	
	@Column(name = "EVENT_CONTENS")
	private String EVENT_CONTENS;
	
    @Column(name ="VENT_STEP_ID") 
	private String VENT_STEP_ID;
	
	@Column(name = "EMAIL_ADDR")
	private String EMAIL_ADDR;
	
	@Column(name = "EVENT_START_DT")
	private LocalDateTime EVENT_START_DT;
	
	@Column(name = "EVENT_END_DT")
	private LocalDateTime EVENT_END_DT;
	
	@Column(name = "REG_DT")
	private LocalDateTime REG_DT;
	
	@Column(name = "MOD_DT")
	private LocalDateTime MOD_DT;
	
	@PrePersist
    public void prePersist() {
        if (this.EVENT_ID == null) {
            this.EVENT_ID = UUID.randomUUID().toString();
        }
    }
}


