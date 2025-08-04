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
        userService.validateExistsById(userNo);

        Room room = roomService.findById(roomNo);

        if(roommateService.isCompletedRoomExists(userNo))
            throw new RestApiException(COMPLETED_ROOM_EXISTS);
        if (roommateService.isUserInRoom(userNo, room))
            throw new RestApiException(USER_IN_ROOM);
        if (roomRequestService.isDuplicateJoinRequest(userNo, room))
            throw new RestApiException(DUPLICATE_JOIN_REQUEST);

        roomRequestService.create(userNo, room, request, Direction.USER_TO_ROOM);
    }
}
