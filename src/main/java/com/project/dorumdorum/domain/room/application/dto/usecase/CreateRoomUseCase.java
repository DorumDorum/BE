package com.project.dorumdorum.domain.room.application.dto.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRole;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CreateRoomUseCase {

    private final RoomService roomService;
    private final UserService userService;
    private final RoommateService roommateService;

    public void execute(Long userNo, RoomCreateRequest request) {
        if(userService.existsById(userNo))
            throw new RestApiException(_NOT_FOUND);

        Room room = roomService.create(userNo, request);
        roommateService.create(userNo, room, RoomRole.HOST);
    }
}
