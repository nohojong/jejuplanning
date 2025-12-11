package net.codecraft.jejutrip.board.post.repository;

import net.codecraft.jejutrip.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> , CustomPostRepository {
    void deletePostByPostId(Long postId);
}