package net.codecraft.jejutrip.s3.service;

import net.codecraft.jejutrip.board.attachment.dto.AttachmentResponse;
import net.codecraft.jejutrip.board.attachment.repository.AttachmentRepository;
import net.codecraft.jejutrip.s3.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static net.codecraft.jejutrip.common.response.message.S3Message.INVALID_FILE;
import static net.codecraft.jejutrip.common.response.message.S3Message.INVALID_IMAGE;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final AttachmentRepository attachmentRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public String uploadFileToS3(MultipartFile file, String uuid) throws IOException {
        if(!isValidFile(file)) {
            throw new S3Exception(INVALID_FILE);
        }

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uuid)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return getS3Url(uuid);
        } catch (Exception e) {
            throw new IOException("Failed to upload file to S3", e);
        }
    }

    public String uploadImageToS3(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();

        if(!isValidImage(file)) {
            throw new S3Exception(INVALID_IMAGE);
        }

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uuid)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return getS3Url(uuid);
        } catch (Exception e) {
            throw new IOException("Failed to upload image to S3", e);
        }
    }

    private String getS3Url(String key) {
        try {
            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            URL url = s3Client.utilities().getUrl(getUrlRequest);
            return url.toString();
        } catch (Exception e) {
            // Fallback to manual URL construction
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
        }
    }

    public boolean isValidFile(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return false;
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();

        if(extension.equals("exe") || extension.equals("bat")) { //윈도우 실행파일 제외
            return false;
        }

        return true;
    }

    public boolean isValidImage(MultipartFile file) {
        if (file == null || file.getOriginalFilename() == null) {
            return false;
        }

        String fileName = file.getOriginalFilename();
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return false;
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();

        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");
    }

    public void deleteFile(String fileName) {
        try {
            // 객체 존재 확인
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.headObject(headObjectRequest);

            // 객체가 존재하면 삭제
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (NoSuchKeyException e) {
            // 파일이 존재하지 않음 - 무시
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    @Transactional
    public void deleteFileByPostId(long postId) {
        List<AttachmentResponse> attachmentResponses = attachmentRepository.findAttachmentsByPostId(postId);
        for(AttachmentResponse attachmentResponse : attachmentResponses) {
            deleteFile(attachmentResponse.getUuidFileName());
        }
    }
}