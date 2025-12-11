package net.codecraft.jejutrip.account.profile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.codecraft.jejutrip.account.profile.domain.Profile;
import net.codecraft.jejutrip.account.user.domain.User;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String email;
    private String phone;
    private int options;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd" , timezone = "Asia/Seoul")
    private LocalDate joinDate;

    public static ProfileResponse createProfileResponse(Profile profile , User user) {
        return ProfileResponse.builder()
                .email(user.getEmail())
                .phone(profile.getPhone())
                .options(profile.getOptions())
                .joinDate(user.getJoinDate())
                .build();
    }
}