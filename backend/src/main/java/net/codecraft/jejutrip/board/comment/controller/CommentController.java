package net.codecraft.jejutrip.board.comment.controller;

import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.board.comment.dto.CommentRequest;
import net.codecraft.jejutrip.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/{postId}")
    public ResponseEntity<ResponseMessage> getComment(@PathVariable Long postId) {
        return ResponseEntity.ok().body(commentService.getCommit(postId));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addComment(@RequestBody CommentRequest commentRequest
            , @CookieValue String accessToken) throws AccountException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(commentRequest , accessToken));
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<ResponseMessage> deleteComment(@PathVariable long commentId , @CookieValue String accessToken) {
        return ResponseEntity.ok().body(commentService.deleteCommentByCommentId(commentId , accessToken));
    }

    @GetMapping(value = "/notifications")
    public ResponseEntity<ResponseMessage> getNotificationFromUser(@CookieValue String accessToken) throws AccountException {
        return ResponseEntity.ok().body(commentService.getNotificationFromUser(accessToken));
    }
}