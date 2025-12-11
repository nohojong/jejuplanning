package net.codecraft.jejutrip.board.attachment.repository;

import net.codecraft.jejutrip.board.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long>, CustomAttachmentRepository {
}
