package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.image.domain.service.ImageService;
import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
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
public class WriteNoticeUseCase {

    private final UserService userService;
    private final NoticeService noticeService;
    private final RoomService roomService;
    private final ImageService imageService;
    private final S3PresignedUrlService s3PresignedUrlService;

    @Transactional
    public NoticeResponse execute(Long userNo, WriteNoticeRequest request) {
        userService.validateExistsById(userNo);

        if(request.title().isEmpty())
            throw new RestApiException(GlobalErrorStatus.TITLE_IS_EMPTY);
        if(request.content().isEmpty())
            throw new RestApiException(GlobalErrorStatus.CONTENT_IS_EMPTY);

        Room room = roomService.findById(request.roomNo());
        if(!room.isHost(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);

        Notice notice = noticeService.writeNotice(userNo, room.getRoomNo(), request);

        String imageUploadUrl = null;
        String imageDownloadUrl = null;
        String imageFileName = null;
        Long imageFileSize = null;

        if (hasImageInfo(request)) {
            if (request.imageFileSize() == null)
                throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);

            imageFileName = request.imageFileName();
            imageFileSize = request.imageFileSize();

            S3PresignedUrlService.UploadUrlInfo uploadInfo =
                    s3PresignedUrlService.generateUploadPresignedUrl(room.getRoomNo(), imageFileName);

            imageService.saveNoticeImage(notice.getNoticeNo(), uploadInfo.s3Key(), imageFileName, imageFileSize);

            imageUploadUrl = uploadInfo.uploadUrl();
            imageDownloadUrl = s3PresignedUrlService.generateDownloadPresignedUrl(uploadInfo.s3Key());
        }

        return NoticeResponse.create(notice, Collections.emptyList(), imageUploadUrl, imageDownloadUrl, imageFileName, imageFileSize);

    }

    private boolean hasImageInfo(WriteNoticeRequest request) {
        return request.imageFileName() != null && !request.imageFileName().isBlank();
    }
}
