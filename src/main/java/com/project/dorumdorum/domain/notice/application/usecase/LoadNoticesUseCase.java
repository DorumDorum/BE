package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.image.domain.entity.Image;
import com.project.dorumdorum.domain.image.domain.service.ImageService;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticesResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.infra.S3PresignedUrlService;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadNoticesUseCase {

    private final UserService userService;
    private final RoommateService roommateService;
    private final RoomService roomService;
    private final NoticeService noticeService;
    private final ImageService imageService;
    private final S3PresignedUrlService s3PresignedUrlService;

    public List<NoticesResponse> loadNotices(Long userNo, Long roomNo) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);
        if(!roommateService.isUserInRoom(userNo, room))
            throw new RestApiException(GlobalErrorStatus.USER_NOT_IN_ROOM);

        return noticeService.loadNoticeList(room.getRoomNo());
    }

    public NoticeResponse loadNotice(Long userNo, Long noticeNo) {
        userService.validateExistsById(userNo);

        Notice notice = noticeService.findById(noticeNo);
        Image image = imageService.findByNoticeNo(noticeNo).orElse(null);

        String downloadUrl = null;
        String fileName = null;
        Long fileSize = null;

        if (image != null) {
            downloadUrl = s3PresignedUrlService.generateDownloadPresignedUrl(image.getS3Key());
            fileName = image.getFileName();
            fileSize = image.getFileSize();
        }

        return NoticeResponse.create(notice, null, downloadUrl, fileName, fileSize);
    }
}
