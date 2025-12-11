package net.codecraft.jejutrip.board.attachment.repository;

import net.codecraft.jejutrip.board.attachment.dto.AttachmentResponse;

import java.util.List;

public interface CustomAttachmentRepository {
    List<AttachmentResponse> findAttachmentsByPostId(long postId);
}
