package net.codecraft.jejutrip.common.test;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.profile.domain.Profile;
import net.codecraft.jejutrip.account.user.constant.UserRole;
import net.codecraft.jejutrip.account.user.constant.UserType;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
@RequiredArgsConstructor
@org.springframework.context.annotation.Profile("local")
public class TestUserGenerator implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!userRepository.existsByEmail("codecraft@example.com")) {
            User admin = User.builder()
                    .email("codecraft@example.com")
                    .password(passwordEncoder.encode("code1234"))
                    .profile(Profile.createInitProfileSetting())
                    .lastLoginDate(LocalDate.now())
                    .isSuspension(false)
                    .role(UserRole.MANAGER)
                    .userType(UserType.GENERAL_USER)
                    .isDelete(false)
                    .build();
            userRepository.save(admin);
        }

        if(!userRepository.existsByEmail("hello@google.com")) {
            User user1 = User.builder()
                    .email("hello@google.com")
                    .password(passwordEncoder.encode("jejutrip#2025"))
                    .profile(Profile.createInitProfileSetting())
                    .lastLoginDate(LocalDate.now())
                    .isSuspension(false)
                    .role(UserRole.USER)
                    .userType(UserType.GENERAL_USER)
                    .isDelete(false)
                    .build();
            userRepository.save(user1);
        }
    }
}