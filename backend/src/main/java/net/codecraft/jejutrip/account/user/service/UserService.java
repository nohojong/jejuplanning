package net.codecraft.jejutrip.account.user.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.user.constant.UserType;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.dto.LoginRequest;
import net.codecraft.jejutrip.account.user.dto.PasswordRequest;
import net.codecraft.jejutrip.account.user.dto.UserResponse;
import net.codecraft.jejutrip.account.user.exception.LoginException;
import net.codecraft.jejutrip.account.user.repository.UserRepository;
import net.codecraft.jejutrip.board.post.repository.PostRepository;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.common.response.message.AccountMessage;
import net.codecraft.jejutrip.s3.service.S3Service;
import net.codecraft.jejutrip.security.jwt.dto.Token;
import net.codecraft.jejutrip.security.jwt.service.JwtService;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import net.codecraft.jejutrip.security.jwt.support.JwtTokenProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Service s3Service;
    private final JwtService jwtService;
    private final CookieSupport cookieSupport;
    private final PasswordEncoder bCryptPasswordEncoder;

    public void validNewAccountVerification(LoginRequest loginRequest) {
        if(userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            throw new LoginException(AccountMessage.EXISTS_ACCOUNT);
        }
    }

    public ResponseMessage register(LoginRequest loginRequest) throws IOException {
        validNewAccountVerification(loginRequest);
        userRepository.save(
                User.createGeneralUser(loginRequest, bCryptPasswordEncoder.encode(loginRequest.getPassword()))
        );
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public void isValidAccount(LoginRequest request, User user) {
        if(user.getUserType().equals(UserType.OAUTH_USER)) {
            throw new LoginException(AccountMessage.NOT_FOUNT_ACCOUNT);
        }
        if(!user.checkPassword(request.getPassword() , bCryptPasswordEncoder)) {
            throw new LoginException(AccountMessage.NOT_MATCH_PASSWORD);
        }
        if(user.isDelete()) {
            throw new LoginException(AccountMessage.IS_DELETE_ACCOUNT);
        }
        if(user.isSuspension() && user.getSuspensionDate().compareTo(LocalDate.now()) > 0) {
            throw new LoginException(
                    "해당 계정은 " + user.getSuspensionDate() + " 일 까지 정지입니다." +
                            " \n사유 : " + user.getSuspensionReason()
            );
        }
    }

    public User login(LoginRequest loginRequest , HttpServletResponse response) {
        User result = findUserByEmail(loginRequest.getEmail());
        isValidAccount(loginRequest, result);
        result.updateLoginDate();
        createJwtToken(result , response);
        return result;
    }

    public void createJwtToken(User user , HttpServletResponse response) {
        Token token = jwtTokenProvider.createJwtToken(user.getUsername(), user.getRole());
        jwtService.login(token);
        cookieSupport.setCookieFromJwt(token , response);
    }

    public void isExistAccount(String email) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new LoginException(AccountMessage.EXISTS_EMAIL);
        };
    }

    public ResponseMessage findUserByToken(String token) {
        UserResponse userResponse = UserResponse.createResponse(findUserByAccessToken(token));
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , userResponse);
    }

    public ResponseMessage removeUser(String accessToken , HttpServletResponse response) {
        User result = findUserByAccessToken(accessToken);
        deleteAllS3FilesUploadedByUserEmail(result.getEmail());
        result.deleteUser();
        cookieSupport.deleteJwtTokenInCookie(response);
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public void deleteAllS3FilesUploadedByUserEmail(String userEmail) {
        List<Long> postIds = postRepository.findPostIdByUserEmail(userEmail);

        for(long postId : postIds) {
            s3Service.deleteFileByPostId(postId);
        }
    }

    public User findUserByAccessToken(String accessToken) {
        String email = jwtTokenProvider.getUserPk(accessToken);
        return findUserByEmail(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new LoginException(AccountMessage.NOT_FOUNT_ACCOUNT));
    }

//    @Transactional
//    public String modifyProfileImage(String accessToken, MultipartFile multipartFile) throws IOException {
//        User result = findUserByAccessToken(accessToken);
//
//        if(result.getProfileImage() != null && !result.getProfileImage().isEmpty()) {
//            s3Service.deleteFile(result.getProfileImage());
//        }
//
//        String url = s3Service.uploadImageToS3(multipartFile);
//
//        result.updateProfileImage(url);
//
//        return url;
//    }

    @Transactional
    public void modifyPassword(PasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LoginException(AccountMessage.NOT_FOUNT_ACCOUNT));
        user.updatePassword(bCryptPasswordEncoder.encode(request.getPassword()));
    }
}