package com.project.dorumdorum.domain.notice.infra;

import com.project.dorumdorum.global.properties.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3PresignedUrlService {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final S3Properties s3Properties;

    // Presigned URL 유효 시간
    private static final Duration UPLOAD_URL_EXPIRATION = Duration.ofMinutes(5);
    private static final Duration DOWNLOAD_URL_EXPIRATION = Duration.ofHours(1);

    // Notice 이미지 저장 경로 prefix
    private static final String NOTICE_IMAGE_PREFIX = "notice/images/";

    public UploadUrlInfo generateUploadPresignedUrl(Long roomNo, String fileName) {
        String key = generateNoticeImageKey(roomNo, fileName);
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(UPLOAD_URL_EXPIRATION)
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String url = presignedRequest.url().toString();

        log.info("Generated upload presigned URL for roomNo: {}, key: {}", roomNo, key);
        
        return new UploadUrlInfo(url, key);
    }

    public String generateDownloadPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(DOWNLOAD_URL_EXPIRATION)
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        String url = presignedRequest.url().toString();

        log.info("Generated download presigned URL for key: {}", key);
        
        return url;
    }


    public void deleteObject(String key) {
        if (key == null || key.isEmpty()) {
            log.warn("S3 key is null or empty, skipping deletion");
            return;
        }

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Successfully deleted S3 object with key: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete S3 object with key: {}", key, e);
            throw new RuntimeException("S3 객체 삭제에 실패했습니다.", e);
        }
    }

    private String generateNoticeImageKey(Long roomNo, String fileName) {
        String uuid = UUID.randomUUID().toString();
        String sanitizedFileName = sanitizeFileName(fileName);
        return String.format("%s%d/%s_%s", NOTICE_IMAGE_PREFIX, roomNo, uuid, sanitizedFileName);
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "image.jpg";
        }
        // 특수문자를 언더스코어로 변경, 공백 제거
        return fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_")
                .replaceAll("\\s+", "")
                .toLowerCase();
    }

    public record UploadUrlInfo(
            String uploadUrl,
            String s3Key
    ) {}
}

