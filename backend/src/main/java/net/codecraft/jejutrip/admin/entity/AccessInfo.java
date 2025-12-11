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
@Table(name = "AccessInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccessInfo {
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "access_id", nullable = false, updatable = false)
    private String ACCESS_ID;
    
    @Column(name = "SEQ")
    private Double SEQ;
    
    @Column(name = "ACCESS_DT")
    private LocalDateTime ACCESS_DT;
    
    @Column(name = "ACCESS_IP")
    private String ACCESS_IP;
    
    @Column(name = "ACCESS_PAGE")
    private String ACCESS_PAGE;
    
    @Column(name = "DETAIL_PAGE_YN")
    private String  DETAIL_PAGE_YN;
    
    @Column(name = "ACCESS_CNT")
    private Double  ACCESS_CNT;
    
    @PrePersist
    public void prePersist() {
        if (this.ACCESS_ID == null) {
            this.ACCESS_ID = UUID.randomUUID().toString();
        }
    }
}