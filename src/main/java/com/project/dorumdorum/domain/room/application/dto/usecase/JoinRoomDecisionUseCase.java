package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.entity.RoomRole;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinRoomDecisionUseCase {

    private final RoomRequestService roomRequestService;
    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    public void approve(Long userNo, Long roomNo, Long roomRequestNo) {
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);
        room.plusCurrentMate();

        roommateService.create(userNo, room, RoomRole.MEMBER);

        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
        // 지원자에게 알림 roomRequest.getUserNo()
        roomRequestService.delete(roomRequest);
    }

    public void reject(Long userNo, Long roomRequestNo) {
        userService.validateExistsById(userNo);
        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
        // 지원자에게 알림 roomRequest.getUserNo()
        roomRequestService.delete(roomRequest);
    }
}
