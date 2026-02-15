package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRequest;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DecideApplicationRequestUseCase {

    private final RoomRequestService roomRequestService;
    private final UserService userService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    public void approve(String userNo, String roomNo, String roomRequestNo) {
        // 유저 존재 유무 검증
        userService.validateExistsById(userNo);

        // 지원자가 이미 속한 방이 있는지 검증
        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
        if(roommateService.existsByUserNo(roomRequest.getUserNo()))
            throw new RestApiException(ALREADY_JOINED_USER);

        // 방장인지 확인
        Room room = roomService.findById(roomNo);
        if(!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        // 방 인원수 +1
        room.plusCurrentMate();
        roommateService.create(roomRequest.getUserNo(), room, RoomRole.MEMBER);

        // todo: 지원자에게 알림 roomRequest.getUserNo()

        // 모든 플로우를 거쳤다면 요청은 삭제
        roomRequestService.delete(roomRequest);
    }

    public void reject(String userNo, String roomRequestNo) {
        // 유저 존재 유무 검증
        userService.validateExistsById(userNo);

        RoomRequest roomRequest = roomRequestService.findById(roomRequestNo);
        if(!roommateService.isHost(userNo, roomRequest.getRoom()))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        // todo: 지원자에게 알림 roomRequest.getUserNo()
        roomRequestService.delete(roomRequest);
    }
}
