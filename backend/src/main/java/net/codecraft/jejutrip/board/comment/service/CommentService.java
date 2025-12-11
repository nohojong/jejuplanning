package net.codecraft.jejutrip.board.comment.service;

import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.common.response.message.CommentMessage;
import net.codecraft.jejutrip.board.comment.dto.CommentResponse;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.service.UserService;
import net.codecraft.jejutrip.board.comment.domain.Comment;
import net.codecraft.jejutrip.board.comment.dto.CommentRequest;
import net.codecraft.jejutrip.board.comment.exception.CommentException;
import net.codecraft.jejutrip.board.comment.repository.CommentRepository;
import net.codecraft.jejutrip.board.post.domain.Post;
import net.codecraft.jejutrip.board.post.service.PostService;
import net.codecraft.jejutrip.security.jwt.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public ResponseMessage<List<Comment>> getCommit(Long postId) {
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , commentRepository.findCommentByPostId(postId));
    }

    @Transactional
    public ResponseMessage addComment(CommentRequest commentRequest , String token) {
        User user = userService.findUserByAccessToken(token);
        Post post = postService.findPostById(commentRequest.getPostId());

        Comment comment = Comment.createComment(commentRequest , user);
        post.addFreeCommit(comment);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    @Transactional
    public ResponseMessage deleteCommentByCommentId(long commentId , String token) {
        String userId = jwtTokenProvider.getUserPk(token);

        Comment comment = commentRepository.findCommentByCommentIdAndUserId(commentId , userId)
                .orElseThrow(() -> new CommentException(CommentMessage.ONLY_OWNER_CAN_DELETE));

        comment.deleteComment();

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public ResponseMessage<List<CommentResponse>> getNotificationFromUser(String token) {
        User user = userService.findUserByAccessToken(token);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS, commentRepository.findCommentByCommentPostWithoutMe(user));
    }
}