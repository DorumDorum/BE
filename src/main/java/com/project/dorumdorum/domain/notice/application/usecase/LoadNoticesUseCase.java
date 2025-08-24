package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.response.LoadNoticesResponse;
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

    public List<LoadNoticesResponse> execute(Long userNo, Long roomNo) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);
        if(!roommateService.isUserInRoom(userNo, room))
            throw new RestApiException(GlobalErrorStatus.USER_NOT_IN_ROOM);

        return noticeService.loadNoticeList(room);
    }
}
