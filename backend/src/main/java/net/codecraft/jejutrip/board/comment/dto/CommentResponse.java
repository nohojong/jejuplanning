package net.codecraft.jejutrip.board.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    private long postId;

    private long commentId;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:MM:SS" , timezone = "Asia/Seoul")
    private Date postDate;

    private String writer;

    private String writerImage;

    private boolean writerIsDelete;

    public CommentResponse(Long commentId, Long postId, String content,
                           Date createdDate, Long writerId, Boolean isMine) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
        this.postDate = createdDate; // 생성일자를 postDate로 사용
        this.writer = "작성자ID:" + writerId; // 임시값 (writerId → String으로 가공)
        this.writerImage = null;
        this.writerIsDelete = false; // 또는 isMine 값 활용 가능
    }
}