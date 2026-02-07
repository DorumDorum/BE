package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMyRoomsUseCase {

    private final RoomService roomService;

    public FindRoomsResponse execute(String userNo) {
        return roomService.findMyRoom(userNo);
    }
}
