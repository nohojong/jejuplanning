package net.codecraft.jejutrip.board.comment.repository;

import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.board.comment.domain.Comment;
import net.codecraft.jejutrip.board.comment.dto.CommentResponse;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {
    Optional<Comment> findCommentByCommentIdAndUserId(long commentId , String userId);
    List<CommentResponse> findCommentByPostId(long postId);
    List<CommentResponse> findCommentByCommentPostWithoutMe(User user);
}