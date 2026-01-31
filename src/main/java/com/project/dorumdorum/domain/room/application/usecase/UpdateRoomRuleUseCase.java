package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@RequiredArgsConstructor
public class UpdateRoomRuleUseCase {

    private final RoomService roomService;
    private final RoomRuleService roomRuleService;

    public RoomRule execute(Long userNo, Long roomNo, UpdateRoomRuleRequest request) {
        if (!roomService.isHost(userNo, roomNo))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        return roomRuleService.update(roomNo, request);
    }
}
