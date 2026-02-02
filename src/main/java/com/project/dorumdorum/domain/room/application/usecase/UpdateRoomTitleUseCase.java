package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRoomTitleUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;

    public void execute(Long userNo, Long roomNo, UpdateRoomTitleRequest request) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        room.updateTitle(request.title().trim());
    }
}
