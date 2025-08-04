package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.InviteRoomRequest;
import com.project.dorumdorum.domain.room.domain.entity.Direction;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InviteRoomRequestUseCase {

    private final UserService userService;
    private final RoomService roomService;
    private final RoomRequestService roomRequestService;

    public void execute(Long userNo, Long roomNo, Long toUser, InviteRoomRequest request) {
        userService.validateExistsById(userNo);
        userService.validateExistsById(toUser);

        Room room = roomService.findById(roomNo);

        roomRequestService.create(toUser, room, request, Direction.ROOM_TO_USER);
        // toUser에게 알림
    }
}
