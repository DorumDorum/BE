package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.WriteNoticeResponse;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WriteNoticeUseCase {

    private final UserService userService;
    private final NoticeService noticeService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    @Transactional
    public WriteNoticeResponse execute(Long userNo, WriteNoticeRequest request) {
        userService.validateExistsById(userNo);

        if(request.title().isEmpty())
            throw new RestApiException(GlobalErrorStatus.TITLE_IS_EMPTY);
        if(request.content().isEmpty())
            throw new RestApiException(GlobalErrorStatus.CONTENT_IS_EMPTY);

        Room room = roomService.findById(request.roomNo());
        if(!room.isHost(userNo))
            throw new RestApiException(GlobalErrorStatus.ROOM_NOT_FOUND);


        return WriteNoticeResponse.create(noticeService.writeNotice(userNo, room, request));

    }
}
