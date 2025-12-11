package net.codecraft.jejutrip.board.comment.domain;

import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.board.comment.dto.CommentRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.codecraft.jejutrip.common.BooleanConverter;
import net.codecraft.jejutrip.board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Post post;

    @Column(nullable = false , length = 200)
    private String content;

    @Column(nullable = false)
    @Convert(converter = BooleanConverter.class)
    private boolean isDelete;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date postDate;

    //@OneToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    static public Comment createComment(CommentRequest commentRequest , User user) {
        return Comment.builder()
                .content(commentRequest.getContent())
                .writer(user)
                .isDelete(false)
                .build();
    }

    public void deleteComment() {
        this.isDelete = true;
    }

}