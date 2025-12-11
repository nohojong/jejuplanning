package net.codecraft.jejutrip.account.profile.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.profile.dto.ProfileRequest;
import net.codecraft.jejutrip.account.profile.service.ProfileService;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping(value = "/statistics")
    public ResponseEntity<ResponseMessage> getStatistics(@CookieValue String accessToken) {
        return ResponseEntity.ok().body(profileService.getStatistics(accessToken));
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> updateProfile(@RequestBody ProfileRequest profileRequest , @CookieValue String accessToken , HttpServletResponse response) {

        return ResponseEntity.ok().body(profileService.updateProfile(profileRequest , accessToken , response));
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getProfileData(@CookieValue String accessToken) {
        return ResponseEntity.ok().body(profileService.getProfileFromUser(accessToken));
    }
}
