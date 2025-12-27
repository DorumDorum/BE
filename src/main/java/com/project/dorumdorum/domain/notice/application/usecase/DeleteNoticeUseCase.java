package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.image.domain.entity.Image;
import com.project.dorumdorum.domain.image.domain.service.ImageService;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.infra.S3PresignedUrlService;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteNoticeUseCase {

    private final UserService userService;
    private final NoticeService noticeService;
    private final ImageService imageService;
    private final S3PresignedUrlService s3PresignedUrlService;

    @Transactional
    public void execute(Long userNo, Long noticeNo) {
        userService.validateExistsById(userNo);

        Notice notice = noticeService.findById(noticeNo);

        if (notice.isDeleted())
            throw new RestApiException(GlobalErrorStatus.NOTICE_ALREADY_DELETED);
        if (!notice.isWriter(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);

        Image image = imageService.findByNoticeNo(noticeNo).orElse(null);
        if (image != null) {
            s3PresignedUrlService.deleteObject(image.getS3Key()); // 버킷에는 하드 삭제
            imageService.softDelete(image); // image는 소프트 삭제
        }

        noticeService.softDelete(notice); // notice 소프트 삭제
    }
}
