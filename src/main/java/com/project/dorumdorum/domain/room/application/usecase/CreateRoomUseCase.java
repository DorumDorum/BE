package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.entity.RoomRole;
import com.project.dorumdorum.domain.room.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateRoomUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRuleService roomRuleService;

    public void execute(Long userNo, RoomCreateRequest request) {
        Room room = roomService.create(userNo, request);
        roommateService.create(userNo, room, RoomRole.HOST);
        roomRuleService.create(room.getRoomNo(), request.rule());
    }
}
