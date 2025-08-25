package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateNoticeUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final NoticeService noticeService;

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

        return noticeService.updateNotice(notice, request);
    }
}
