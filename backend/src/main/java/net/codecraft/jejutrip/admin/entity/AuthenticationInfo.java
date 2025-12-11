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
@Table(name = "AuthenticationInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticationInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
    
    @Column(name = "PWD")
    private String pwd;
    
    @Column(name = "LOGIN_REG_DT")
    private LocalDateTime LOGIN_REG_DT;
    
    @Column(name = "FAST_LOGIN_YN")
    private String FAST_LOGIN_YN;
    
    @Column(name = "SUCCESS_YN")
    private String SUCCESS_YN;
    
    @Column(name = "BLOCKING_YN")
    private String  BLOCKING_YN;
    
    @Column(name = "REG_DT")
    private LocalDateTime  REG_DT;    
    
    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
