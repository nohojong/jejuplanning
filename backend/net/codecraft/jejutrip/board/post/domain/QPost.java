package net.codecraft.jejutrip.board.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1410494181L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final ListPath<net.codecraft.jejutrip.board.attachment.domain.Attachment, net.codecraft.jejutrip.board.attachment.domain.QAttachment> attachments = this.<net.codecraft.jejutrip.board.attachment.domain.Attachment, net.codecraft.jejutrip.board.attachment.domain.QAttachment>createList("attachments", net.codecraft.jejutrip.board.attachment.domain.Attachment.class, net.codecraft.jejutrip.board.attachment.domain.QAttachment.class, PathInits.DIRECT2);

    public final ListPath<net.codecraft.jejutrip.board.comment.domain.Comment, net.codecraft.jejutrip.board.comment.domain.QComment> comment = this.<net.codecraft.jejutrip.board.comment.domain.Comment, net.codecraft.jejutrip.board.comment.domain.QComment>createList("comment", net.codecraft.jejutrip.board.comment.domain.Comment.class, net.codecraft.jejutrip.board.comment.domain.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final BooleanPath isBlockComment = createBoolean("isBlockComment");

    public final BooleanPath isDelete = createBoolean("isDelete");

    public final BooleanPath isPrivate = createBoolean("isPrivate");

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final DateTimePath<java.util.Date> postDate = createDateTime("postDate", java.util.Date.class);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final ListPath<net.codecraft.jejutrip.account.user.domain.User, net.codecraft.jejutrip.account.user.domain.QUser> recommendation = this.<net.codecraft.jejutrip.account.user.domain.User, net.codecraft.jejutrip.account.user.domain.QUser>createList("recommendation", net.codecraft.jejutrip.account.user.domain.User.class, net.codecraft.jejutrip.account.user.domain.QUser.class, PathInits.DIRECT2);

    public final ListPath<net.codecraft.jejutrip.board.tag.domain.Tag, net.codecraft.jejutrip.board.tag.domain.QTag> tag = this.<net.codecraft.jejutrip.board.tag.domain.Tag, net.codecraft.jejutrip.board.tag.domain.QTag>createList("tag", net.codecraft.jejutrip.board.tag.domain.Tag.class, net.codecraft.jejutrip.board.tag.domain.QTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> views = createNumber("views", Integer.class);

    public final net.codecraft.jejutrip.account.user.domain.QUser writer;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.writer = inits.isInitialized("writer") ? new net.codecraft.jejutrip.account.user.domain.QUser(forProperty("writer"), inits.get("writer")) : null;
    }

}

