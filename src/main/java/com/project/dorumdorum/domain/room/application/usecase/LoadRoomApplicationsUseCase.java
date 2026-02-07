package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomRequestService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadRoomApplicationsUseCase {

    private final RoomRequestService roomRequestService;
    private final RoomService roomService;
    private final RoommateService roommateService;

    public List<RoomRequestApplicationResponse> execute(String userNo, String roomNo) {
        Room room = roomService.findById(roomNo);

        if (!roommateService.isUserRoommate(userNo, roomNo)) {
            throw new RestApiException(NO_PERMISSION_ON_ROOM);
        }

        return roomRequestService.findApplicationsByRoom(room);
    }
}
