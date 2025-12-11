package net.codecraft.jejutrip.security.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String email) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeKey,
                                     Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeKey, attributes);
        }

        throw new IllegalArgumentException("Unknown registration id: " + registrationId);
    }

    private static OAuthAttributes ofNaver(String userNameAttributeKey,
                                           Map<String, Object> attributes) {
        Object responseObj = attributes.get("response");

        if (!(responseObj instanceof Map)) {
            throw new IllegalStateException("네이버 응답 형식이 올바르지 않습니다.");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) responseObj;

        String email = (String) response.get("email");
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalStateException("이메일 정보를 찾을 수 없습니다.");
        }

        return OAuthAttributes.builder()
                .email(email)
                .attributes(response)
                .nameAttributeKey(userNameAttributeKey)
                .build();
    }
}