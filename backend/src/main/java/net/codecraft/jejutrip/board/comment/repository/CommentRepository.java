package net.codecraft.jejutrip.board.comment.repository;

import net.codecraft.jejutrip.board.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> , CustomCommentRepository {
}