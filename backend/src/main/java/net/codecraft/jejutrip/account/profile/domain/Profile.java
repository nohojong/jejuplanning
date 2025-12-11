package net.codecraft.jejutrip.account.profile.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.codecraft.jejutrip.account.profile.dto.ProfileRequest;

@Entity
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    @Column(nullable = false)
    private int options;

    static public Profile createInitProfileSetting() {
        Profile profile = new Profile();
        profile.setOptions(1);
        return profile;
    }

    public void updateProfile(ProfileRequest profileRequest) {
        this.phone = profileRequest.getPhone();
        this.options = profileRequest.getOptions();
    }
}
