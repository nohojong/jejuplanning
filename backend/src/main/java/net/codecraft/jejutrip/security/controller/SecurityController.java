package net.codecraft.jejutrip.security.controller;

import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping(value = "/authorization/denied")
    public ResponseEntity<ResponseMessage> informAuthorizationDenied() {
        ResponseMessage message = ResponseMessage.of(
                ResponseCode.AUTHORIZATION_ERROR , "로그인이 필요한 서비스입니다."
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }
}