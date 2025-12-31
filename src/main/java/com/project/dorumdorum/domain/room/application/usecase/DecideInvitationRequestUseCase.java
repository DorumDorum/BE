package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecideInvitationRequestUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRequestService roomRequestService;

    public void approve(Long userNo, Long roomRequestNo, Long roomNo) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);

        // 모레 이어서 개발해둘테니 여긴 신경쓰지 말아줘요 .. 졸려서 뇌사왔네요
        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
//        if(roommateService.isCompletedRoomExists())
    }
}
