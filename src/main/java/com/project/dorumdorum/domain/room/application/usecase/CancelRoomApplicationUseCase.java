package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CancelRoomApplicationUseCase {

    private final RoomService roomService;
    private final RoomRequestService roomRequestService;

    public void execute(String userNo, String roomNo) {
        Room room = roomService.findById(roomNo);
        roomRequestService.cancelJoinRequest(userNo, room);
    }
}

