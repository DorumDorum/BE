package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteNoticeUseCase {

    private final UserService userService;
    private final NoticeService noticeService;

    public void execute(Long userNo, Long noticeNo) {
        userService.validateExistsById(userNo);

        Notice notice = noticeService.findById(noticeNo);

        if(notice.isDeleted())
            throw new RestApiException(GlobalErrorStatus.NOTICE_ALREADY_DELETED);
        if(!notice.isWriter(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);

        noticeService.deleteNotice(noticeNo);
    }
}
