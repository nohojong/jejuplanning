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
import lombok.*;

@Entity
@Table(name = "logininfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
    
    @Column(name = "INIT_PWD_DT")
    private LocalDateTime INIT_PWD_DT;
    
    @Column(name = "FAIL_PWD_CNT")
    private double FAIL_PWD_CNT;
    
    @Column(name = "LOGIN_REG_DT")
    private LocalDateTime	LOGIN_REG_DT;
   
    @Column(name = "FAST_LOGIN_YN")
    private String FAST_LOGIN_YN;
    
    @Column(name = "DEL_YN")
    private String DEL_YN;
    
    @Column(name = "LAST_ACCESS_DT")
    private String LAST_ACCESS_DT;
    
    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }

    
}
