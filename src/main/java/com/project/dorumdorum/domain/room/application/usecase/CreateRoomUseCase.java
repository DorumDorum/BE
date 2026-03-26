package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.entity.RoomRole;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
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
    private final RoomRuleMapper roomRuleMapper;

    public String execute(String userNo, RoomCreateRequest request) {
        Room room = roomService.create(userNo, request);
        roommateService.create(userNo, room, RoomRole.HOST);

        RoomRule roomRule = roomRuleMapper.toRoomRule(room, request.rule());
        roomRuleService.save(roomRule);
        return room.getRoomNo();
    }
}
