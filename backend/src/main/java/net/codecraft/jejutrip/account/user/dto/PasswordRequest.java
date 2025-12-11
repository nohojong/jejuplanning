package net.codecraft.jejutrip.account.user.dto;

import lombok.Getter;

@Getter
public class PasswordRequest {
    private String email;
    private String password;
}