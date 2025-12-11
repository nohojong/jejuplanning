package net.codecraft.jejutrip.admin.entity;

import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name = "Travel_F")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Travel_F {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FAC_ID", nullable = false, updatable = false)
	private String FAC_ID;
	 
	@Column(name = "TRAVEL_TYPE")
	private String TRAVEL_TYPE;
	
	@Column(name = "TRAVEL_NAME")
	private String TRAVEL_NAME;
	
    @Column(name ="ADDRESS") 
	private String ADDRESS;
	
	@Column(name = "OP_TIME")
	private String OP_TIME;
	
	@Column(name = "TEL")
	private LocalDateTime TEL;
	
	@Column(name = "REMARK")
	private LocalDateTime REMARK;
	
	@Column(name = "SPOT_NAME")
	private LocalDateTime SPOT_NAME;
	
	
	@PrePersist
    public void prePersist() {
        if (this.FAC_ID == null) {
            this.FAC_ID = UUID.randomUUID().toString();
        }
    }
}


