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
@Table(name = "RoleInfo")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleInfo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BEST_RES_ID", nullable = false, updatable = false)
    private String ROLE_ID;
    
    @Column(name = "SEQ")
    private double SEQ;
    
    @Column(name = "USER_ID")
    private String USER_ID;
    
    @Column(name = "ROLE_NM")
    private String ROLE_NM;
    
    @Column(name = "USE_YN")
    private LocalDateTime  USE_YN;
    
    @PrePersist
    public void prePersist() {
        if (this.ROLE_ID == null) {
            this.ROLE_ID = UUID.randomUUID().toString();
        }
    }

}
