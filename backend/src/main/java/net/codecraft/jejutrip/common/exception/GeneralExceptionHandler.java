package net.codecraft.jejutrip.common.exception;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import net.codecraft.jejutrip.account.user.exception.LoginException;
import net.codecraft.jejutrip.account.user.exception.UserNotFoundException;
import net.codecraft.jejutrip.board.comment.exception.CommentException;
import net.codecraft.jejutrip.board.post.exception.PostException;
import net.codecraft.jejutrip.tour.place.exception.PlaceNotFoundException;
import net.codecraft.jejutrip.tour.review.exception.ReviewException;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.security.jwt.exception.InvalidTokenException;
import net.codecraft.jejutrip.security.jwt.support.CookieSupport;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GeneralExceptionHandler {

    private final CookieSupport cookieSupport;

    @ExceptionHandler({InvalidTokenException.class , AuthenticationException.class})
    public ResponseEntity<ResponseMessage> invalidTokenExceptionHandler(Exception e) {
        log.warn("Invalid token or authentication error: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.INVALID_TOKEN , e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler({MalformedJwtException.class})
    public ResponseEntity<ResponseMessage> malformedJwtExceptionHandler(Exception e , HttpServletResponse response) {
        log.warn("Malformed JWT token detected: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.AUTHORIZATION_ERROR , "변조된 RefreshToken 입니다.");
        cookieSupport.deleteJwtTokenInCookie(response);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<ResponseMessage> accountExceptionHandler(Exception e) {
        log.warn("Login exception: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({PostException.class , CommentException.class})
    public ResponseEntity<ResponseMessage> postExceptionHandler(Exception e) {
        log.warn("Post or comment exception: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({FileSizeLimitExceededException.class})
    public ResponseEntity<ResponseMessage> fileUploadExceptionHandler(Exception e) {
        log.warn("File size limit exceeded: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ResponseMessage> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler({UserNotFoundException.class, PlaceNotFoundException.class, ReviewException.class})
    public ResponseEntity<ResponseMessage> resourceNotFoundExceptionHandler(RuntimeException e) {
        log.warn("Resource not found: {}", e.getMessage());
        ResponseMessage message = ResponseMessage.of(ResponseCode.REQUEST_FAIL , e.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

}