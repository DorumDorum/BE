package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinRoomRequestUseCase {

    private final UserService userService;
    private final RoomRequestService roomRequestService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    public void execute(Long userNo, Long roomNo, JoinRoomRequest request) {
        // 유저 존재 유무 검증
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);

        // 확정된 방이 있는 유저인가 검증
        if(roommateService.isCompletedRoomExists(userNo))
            throw new RestApiException(COMPLETED_ROOM_EXISTS);
        // 이미 속한 방인지 검증
        if (roommateService.isUserInRoom(userNo, room))
            throw new RestApiException(USER_IN_ROOM);
        // 이미 보낸 요청인지 검증
        if (roomRequestService.isDuplicateJoinRequest(userNo, room))
            throw new RestApiException(DUPLICATE_JOIN_REQUEST);

        // 요청 생성
        roomRequestService.create(userNo, room, request, Direction.USER_TO_ROOM);
        // todo: 방장에게 알림
    }
}
