package net.codecraft.jejutrip.board.comment.repository;

import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.board.comment.domain.Comment;
import net.codecraft.jejutrip.board.comment.dto.CommentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.codecraft.jejutrip.account.user.domain.QUser;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static net.codecraft.jejutrip.account.user.domain.QUser.user;
import static net.codecraft.jejutrip.board.comment.domain.QComment.comment;
import static net.codecraft.jejutrip.board.post.domain.QPost.post;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findCommentByCommentIdAndUserId(long commentId, String userEmail) {
        Comment result = queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer , user).on(user.email.eq(userEmail))
                .where(comment.commentId.eq(commentId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<CommentResponse> findCommentByPostId(long postId) {
        return queryFactory.select(Projections.constructor(CommentResponse.class ,
                        post.postId , comment.commentId , comment.content , comment.postDate , user.id , /*user.profileImage*/  user.isDelete))
                .from(comment)
                .innerJoin(comment.writer , user)
                .innerJoin(comment.post , post).on(post.postId.eq(postId))
                .where(comment.isDelete.eq(false))
                .fetch();
    }

    @Override
    public List<CommentResponse> findCommentByCommentPostWithoutMe(User userData) {
        QUser subUser = new QUser("subUser");

        return queryFactory.select(Projections.constructor(CommentResponse.class ,
                        post.postId , comment.commentId , comment.content , comment.postDate , user.id , /*user.profileImage*/  user.isDelete))
                .from(comment)
                .innerJoin(comment.writer , user).on(user.ne(userData))
                .innerJoin(comment.post , post)
                .on(post.eqAny(JPAExpressions.select(post)
                        .from(post)
                        .innerJoin(post.writer , subUser).on(subUser.eq(userData))
                        .where(post.isDelete.eq(false))
                ))
                .orderBy(comment.commentId.desc())
                .where(comment.isDelete.eq(false))
                .limit(10)
                .fetch();
    }
}