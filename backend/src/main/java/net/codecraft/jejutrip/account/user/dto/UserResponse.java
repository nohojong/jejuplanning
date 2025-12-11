package net.codecraft.jejutrip.account.user.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.codecraft.jejutrip.account.user.domain.User;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd" , timezone = "Asia/Seoul")
    private LocalDate joinDate;

    private String profileImage;

    private boolean isDelete;

    private int options;

    public static UserResponse createResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .password(user.getPassword())
                .joinDate(user.getJoinDate())
                .isDelete(user.isDelete())
                .options(user.getProfile().getOptions())
                .build();
    }
}