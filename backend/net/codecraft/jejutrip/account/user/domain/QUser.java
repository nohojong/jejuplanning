package net.codecraft.jejutrip.account.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -848217910L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDelete = createBoolean("isDelete");

    public final BooleanPath isSuspension = createBoolean("isSuspension");

    public final DatePath<java.time.LocalDate> joinDate = createDate("joinDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> lastLoginDate = createDate("lastLoginDate", java.time.LocalDate.class);

    public final StringPath password = createString("password");

    public final ListPath<net.codecraft.jejutrip.board.post.domain.Post, net.codecraft.jejutrip.board.post.domain.QPost> post = this.<net.codecraft.jejutrip.board.post.domain.Post, net.codecraft.jejutrip.board.post.domain.QPost>createList("post", net.codecraft.jejutrip.board.post.domain.Post.class, net.codecraft.jejutrip.board.post.domain.QPost.class, PathInits.DIRECT2);

    public final net.codecraft.jejutrip.account.profile.domain.QProfile profile;

    public final EnumPath<net.codecraft.jejutrip.account.user.constant.UserRole> role = createEnum("role", net.codecraft.jejutrip.account.user.constant.UserRole.class);

    public final DatePath<java.time.LocalDate> suspensionDate = createDate("suspensionDate", java.time.LocalDate.class);

    public final StringPath suspensionReason = createString("suspensionReason");

    public final EnumPath<net.codecraft.jejutrip.account.user.constant.UserType> userType = createEnum("userType", net.codecraft.jejutrip.account.user.constant.UserType.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new net.codecraft.jejutrip.account.profile.domain.QProfile(forProperty("profile")) : null;
    }

}

