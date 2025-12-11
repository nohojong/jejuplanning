package net.codecraft.jejutrip.account.profile.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.profile.dto.ProfileRequest;
import net.codecraft.jejutrip.account.profile.dto.StatisticsResponse;
import net.codecraft.jejutrip.account.profile.ropository.ProfileRepository;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.exception.LoginException;
import net.codecraft.jejutrip.account.user.repository.UserRepository;
import net.codecraft.jejutrip.account.user.service.UserService;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.common.response.message.AccountMessage;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import net.codecraft.jejutrip.security.jwt.support.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static net.codecraft.jejutrip.account.profile.dto.ProfileResponse.createProfileResponse;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CookieSupport cookieSupport;

    public ResponseMessage<StatisticsResponse> getStatistics(String accessToken) {
        String userEmail = jwtTokenProvider.getUserPk(accessToken);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS, profileRepository.getStatisticsOfUser(userEmail));
    }

    @Transactional
    public ResponseMessage updateProfile(ProfileRequest profileRequest , String token , HttpServletResponse response) {
        User user = userService.findUserByAccessToken(token);

        if(!profileRequest.getEmail().equals(user.getEmail())) {
            if(userRepository.findByEmail(profileRequest.getEmail()).isPresent()) {
                throw new LoginException(AccountMessage.EXISTS_ID);
            }
            cookieSupport.deleteJwtTokenInCookie(response);
        }

        user.getProfile().updateProfile(profileRequest);
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public ResponseMessage getProfileFromUser(String token) {
        User result = userService.findUserByAccessToken(token);
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS, createProfileResponse(result.getProfile() , result));
    }
}
