package net.codecraft.jejutrip.common.exception;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import net.codecraft.jejutrip.account.user.exception.LoginException;
import net.codecraft.jejutrip.board.comment.exception.CommentException;
import net.codecraft.jejutrip.board.post.exception.PostException;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.security.jwt.exception.InvalidTokenException;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private final CookieSupport cookieSupport = new CookieSupport();

    @ExceptionHandler({InvalidTokenException.class , AuthenticationException.class})
    public ResponseEntity invalidTokenExceptionHandler(Exception e) {
        ResponseMessage message = ResponseMessage.of(ResponseCode.INVALID_TOKEN , e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity malformedJwtExceptionHandler(Exception e , HttpServletResponse response) {
        ResponseMessage message = ResponseMessage.of(ResponseCode.AUTHORIZATION_ERROR , "변조된 RefreshToken 입니다.");
        cookieSupport.deleteJwtTokenInCookie(response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity accountExceptionHandler(Exception e) {
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({PostException.class , CommentException.class})
    public ResponseEntity postExceptionHandler(Exception e) {
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({FileSizeLimitExceededException.class})
    public ResponseEntity fileUploadExceptionHandler(Exception e) {
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());

        return ResponseEntity.badRequest().body(message);
    }

}