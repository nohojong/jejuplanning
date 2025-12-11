package net.codecraft.jejutrip.account.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import net.codecraft.jejutrip.account.profile.domain.Profile;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import net.codecraft.jejutrip.account.user.constant.UserType;
import net.codecraft.jejutrip.account.user.dto.LoginRequest;
import net.codecraft.jejutrip.board.post.domain.Post;
import net.codecraft.jejutrip.common.BooleanConverter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    private LocalDate suspensionDate;

    @Column(nullable = false)
    @Convert(converter = BooleanConverter.class)
    private boolean isSuspension;

    @Column(nullable = false)
    @Convert(converter = BooleanConverter.class)
    private boolean isDelete;

    private String suspensionReason;

    @Column(nullable = false)
    private LocalDate lastLoginDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private UserRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy/MM/dd" , timezone = "Asia/Seoul")
    @Column(nullable = false)
    private LocalDate joinDate;

    @Builder.Default
    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Profile profile = new Profile();

    @Builder.Default
    @OneToMany(mappedBy = "writer" , cascade = CascadeType.ALL)
    private List<Post> post = new ArrayList<>();

    public void addPost(Post post) {
        this.post.add(post);
        post.setWriter(this);
    }

    public void updateLoginDate() {
        lastLoginDate = LocalDate.now();
    }

    public void deleteUser() {
        this.isDelete = true;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.password);
    }

    public void addSuspensionDate(int date, String reason) {
        if(!isSuspension || suspensionDate == null) {
            isSuspension = true;
            suspensionDate = LocalDate.now().plusDays(date);
            suspensionReason = reason;
        }
        suspensionDate = suspensionDate.plusDays(date);
    }

    public void minusSuspensionDate(int date) {
        if(LocalDate.now().compareTo(suspensionDate.minusDays(date)) > 0) {
            isSuspension = false;
            suspensionDate = LocalDate.now();
        }
        suspensionDate = suspensionDate.minusDays(date);
    }


    public static User createGeneralUser(LoginRequest loginRequest, String password) {
        return User.builder()
                .email(loginRequest.getEmail())
                .password(password)
                .profile(Profile.createInitProfileSetting())
                .lastLoginDate(LocalDate.now())
                .isSuspension(false)
                .role(UserRole.USER)
                .userType(UserType.GENERAL_USER)
                .isDelete(false)
                .build();
    }

    public static User createOAuthUser(String email) {
        return User.builder()
                .email(email)
                .profile(Profile.createInitProfileSetting())
                .lastLoginDate(LocalDate.now())
                .isSuspension(false)
                .role(UserRole.USER)
                .userType(UserType.OAUTH_USER)
                .isDelete(false)
                .build();
    }

    // 여기 부터 UserDetails 구현 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRole()));
    }
    @Override
    public String getUsername() { return email; }
    @Override
    public String getPassword() { return password; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}