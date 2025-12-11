package net.codecraft.jejutrip.board.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.board.post.domain.Post;
import net.codecraft.jejutrip.board.post.dto.PostListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static net.codecraft.jejutrip.account.user.domain.QUser.user;
import static net.codecraft.jejutrip.board.comment.domain.QComment.comment;
import static net.codecraft.jejutrip.board.post.domain.QPost.post;
import static net.codecraft.jejutrip.board.tag.domain.QTag.tag;

@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    //    Optional<User> findRecommendationFromPost(Long postId, String userId);
    @Override
    public Optional<User> findRecommendationFromPost(Long postId , String userEmail) {
        User result = queryFactory.select(user)
                .from(post)
                .innerJoin(post.recommendation , user).on(user.email.eq(userEmail))
                .where(post.postId.eq(postId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    //    List<PostListResponse> findPostBySearch(Pageable pageable, String content);
    @Override
    public List<PostListResponse> findPostBySearch(Pageable pageable , String content) {
        return queryFactory.select(Projections.constructor(PostListResponse.class , post.postId , post.title
                        , user.id /*, user.profileImage*/ , user.isDelete , post.postDate , post.likes , post.views , comment.count()))
                .from(post)
                .innerJoin(post.writer , user).on(post.writer.eq(user))
                .leftJoin(post.comment , comment).on(comment.post.eq(post))
                .where(post.content.contains(content).or(post.title.contains(content)).and(post.isDelete.eq(false)))
                .groupBy(post.postId)
                .orderBy(post.postId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    //    List<PostListResponse> findPostByTag(Pageable pageable, String tagData);
    @Override
    public List<PostListResponse> findPostByTag(Pageable pageable , String tagData) {
        return queryFactory.select(Projections.constructor(PostListResponse.class , post.postId , post.title ,
                        user.id /*, user.profileImage*/ , user.isDelete , post.postDate , post.likes , post.views , comment.count()))
                .from(post)
                .innerJoin(post.tag , tag).on(tag.tagData.eq(tagData))
                .innerJoin(post.writer , user).on(post.writer.eq(user))
                .leftJoin(post.comment , comment).on(comment.post.eq(post))
                .groupBy(post.postId)
                .orderBy(post.postId.desc())
                .where(post.isDelete.eq(false))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    //    Optional<Post> findPostByPostId(long postId);
    @Override
    public Optional<Post> findPostByPostId(long postId) {
        Post result = queryFactory.select(post)
                .from(post)
                .join(post.writer , user).fetchJoin()
                .where(post.postId.eq(postId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    //    List<PostListResponse> getPostList(Pageable pageable);
    @Override
    public List<PostListResponse> getPostList(Pageable pageable) {
        return queryFactory.select(Projections.constructor(PostListResponse.class , post.postId
                        , post.title , user.id , /*, user.profileImage*/ user.isDelete , post.postDate , post.likes
                        , post.views , comment.count()))
                .from(post)
                .innerJoin(post.writer , user).on(post.writer.eq(user))
                .leftJoin(post.comment , comment).on(comment.post.eq(post))
                .groupBy(post.postId)
                .where(post.isDelete.eq(false))
                .orderBy(post.postId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    //    long getTotalNumberOfPosts();
    @Override
    public long getTotalNumberOfPosts() {
        return queryFactory.select(post.count())
                .from(post)
                .where(post.isDelete.eq(false))
                .fetchOne();
    }


    //    long getTotalNumberOfTagSearchPosts(String tagData);
    @Override
    public long getTotalNumberOfTagSearchPosts(String tagData) {
        return queryFactory.select(post.count())
                .from(post)
                .innerJoin(post.tag , tag).on(tag.tagData.eq(tagData))
                .where(post.isDelete.eq(false))
                .fetchOne();
    }


    //    long getTotalNumberOfSearchPosts(String search);
    @Override
    public long getTotalNumberOfSearchPosts(String search) {
        return queryFactory.select(post.count())
                .from(post)
                .where(post.title.contains(search).or(post.content.contains(search)).and(post.isDelete.eq(false)))
                .fetchOne();
    }

    //    List<String> findTagsInPostId(long postId);
    @Override
    public List<String> findTagsInPostId(long postId) {
        return queryFactory.select(tag.tagData)
                .from(tag)
                .leftJoin(post).on(post.tag.contains(tag))
                .where(post.postId.eq(postId).and(post.isDelete.eq(false)))
                .fetch();
    }

    //    void updatePostView(long postId);
    @Override
    public void updatePostView(long postId) {
        queryFactory.update(post)
                .set(post.views , post.views.add(1))
                .where(post.postId.eq(postId))
                .execute();
    }

    //    List<Long> findPostIdByUserId(String userId);  String -> Long
    @Override
    public List<Long> findPostIdByUserEmail(String userEmail) {
        return queryFactory.select(post.postId)
                .from(post)
                .innerJoin(post.writer , user).on(user.email.eq(userEmail))
                .where(post.isDelete.eq(false))
                .fetch();
    }
}