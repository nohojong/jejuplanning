package net.codecraft.jejutrip.account.profile.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {

    private String email;
    private String phone;
    private Integer options;

}
