package net.codecraft.jejutrip.account.profile.ropository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.profile.dto.StatisticsResponse;
import net.codecraft.jejutrip.account.user.domain.QUser;
import net.codecraft.jejutrip.board.comment.domain.QComment;
import net.codecraft.jejutrip.board.post.domain.QPost;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ProfileRepositoryImpl implements CustomProfileRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public StatisticsResponse getStatisticsOfUser(String userEmail) {

        long totalPost = queryFactory.select(QPost.post.count())
                .from(QPost.post)
                .innerJoin(QPost.post.writer , QUser.user).on(QUser.user.email.eq(userEmail))
                .fetchOne();

        Integer totalView = queryFactory.select(QPost.post.views.sum())
                .from(QPost.post)
                .innerJoin(QPost.post.writer , QUser.user).on(QUser.user.email.eq(userEmail)).fetchOne();

        if(totalView == null) {
            totalView = 0;
        }

        long totalComment = queryFactory.select(QComment.comment.count())
                .from(QComment.comment)
                .innerJoin(QComment.comment.writer , QUser.user).on(QUser.user.email.eq(userEmail)).fetchOne();

        LocalDate joinDate = queryFactory.select(QUser.user.joinDate)
                .from(QUser.user)
                .where(QUser.user.email.eq(userEmail)).fetchOne();

        return StatisticsResponse.builder()
                .totalPost(totalPost)
                .totalPostView(totalView)
                .totalComment(totalComment)
                .joinData(joinDate.toString())
                .build();
    }
}