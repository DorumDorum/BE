package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.image.domain.entity.Image;
import com.project.dorumdorum.domain.image.domain.service.ImageService;
import com.project.dorumdorum.domain.notice.application.dto.request.UpdateNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.infra.S3PresignedUrlService;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UpdateNoticeUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final NoticeService noticeService;
    private final ImageService imageService;
    private final S3PresignedUrlService s3PresignedUrlService;

    @Transactional
    public NoticeResponse execute(Long userNo, UpdateNoticeRequest request) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(request.roomNo());

        // 방장이 아닌 구성원도 작성가능 할 경우 삭제
        if(!room.isHost(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);

        Notice notice = noticeService.findById(request.noticeNo());

        if(!notice.isWriter(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);

        Image existingImage = imageService.findByNoticeNo(notice.getNoticeNo()).orElse(null);

        boolean deleteImage = Boolean.TRUE.equals(request.deleteImage());
        boolean hasNewImage = hasImageInfo(request);

        if (deleteImage || hasNewImage) {
            if (existingImage != null) {
                s3PresignedUrlService.deleteObject(existingImage.getS3Key());
                imageService.deleteByNoticeNo(notice.getNoticeNo());
                existingImage = null;
            }
        }

        String imageUploadUrl = null;
        String imageDownloadUrl = null;
        String imageFileName = null;
        Long imageFileSize = null;

        if (hasNewImage) {
            if (request.imageFileSize() == null)
                throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);

            imageFileName = request.imageFileName();
            imageFileSize = request.imageFileSize();

            S3PresignedUrlService.UploadUrlInfo uploadInfo =
                    s3PresignedUrlService.generateUploadPresignedUrl(request.roomNo(), imageFileName);

            imageService.saveNoticeImage(notice.getNoticeNo(), uploadInfo.s3Key(), imageFileName, imageFileSize);

            imageUploadUrl = uploadInfo.uploadUrl();
            imageDownloadUrl = s3PresignedUrlService.generateDownloadPresignedUrl(uploadInfo.s3Key());
        } else if (!deleteImage && existingImage != null) {
            imageFileName = existingImage.getFileName();
            imageFileSize = existingImage.getFileSize();
            imageDownloadUrl = s3PresignedUrlService.generateDownloadPresignedUrl(existingImage.getS3Key());
        }

        Notice updatedNotice = noticeService.updateNotice(notice, request);

        return NoticeResponse.create(updatedNotice, Collections.emptyList(), imageUploadUrl, imageDownloadUrl, imageFileName, imageFileSize);
    }

    private boolean hasImageInfo(UpdateNoticeRequest request) {
        return request.imageFileName() != null && !request.imageFileName().isBlank();
    }
}
