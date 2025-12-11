package net.codecraft.jejutrip.board.attachment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.board.attachment.dto.AttachmentResponse;

import java.util.List;

import static net.codecraft.jejutrip.board.attachment.domain.QAttachment.attachment;
import static net.codecraft.jejutrip.board.post.domain.QPost.post;

@RequiredArgsConstructor
public class AttachmentRepositoryImpl implements CustomAttachmentRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AttachmentResponse> findAttachmentsByPostId(long postId) {
        return queryFactory.select(Projections.constructor(AttachmentResponse.class, attachment.attachmentId
                        , attachment.realFileName , attachment.s3Url , attachment.fileSize , attachment.uuidFileName))
                .from(attachment)
                .innerJoin(attachment.post , post).on(post.postId.eq(postId))
                .fetch();
    }
}
