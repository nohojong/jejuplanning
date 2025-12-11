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
import lombok.*;

@Entity
@Table(name = "AuthorizeInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthorizeInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUTH_ID", nullable = false, updatable = false)
    private String AUTH_ID;
    
    @Column(name = "PWD")
    private String pwd;
    
    @Column(name = "SEQ")
    private double SEQ;
    
    @Column(name = "USER_ID")
    private String USER_ID;
    
    @Column(name = "AUTH_NM")
    private String AUTH_NM;
    
    @Column(name = "MENU_ID")
    private String  MENU_ID;
    
    @Column(name = "USE_YN")
    private LocalDateTime  USE_YN;    
    
    @PrePersist
    public void prePersist() {
        if (this.AUTH_ID == null) {
            this.AUTH_ID = UUID.randomUUID().toString();
        }
    }
}
