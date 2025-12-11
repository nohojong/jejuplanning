package net.codecraft.jejutrip.s3.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Component
@Profile("local")
public class S3BucketInitializer {
    private final S3Client s3Client;
    private final String bucketName;

    public S3BucketInitializer(S3Client s3Client, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @PostConstruct
    public void initializeBucket() {
        try {
            // 버킷이 존재하는지 확인
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
        } catch (NoSuchBucketException e) {
            // 버킷이 없으면 생성
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
        }
    }
}