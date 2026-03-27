package com.project.dorumdorum.domain.checklist.application.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import com.project.dorumdorum.domain.room.domain.service.RoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRoomRuleUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    public void execute(String userNo, String roomNo, UpdateRoomRuleRequest request) {
        Room room = roomService.findByIdForUpdate(roomNo);

        if (!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);

        room.updateCapacity(request.capacity());
        room.updateRoomType(request.roomType());
        room.updateResidencePeriod(request.residencePeriod());

        roomRuleMapper.updateRoomRule(request, roomRule);
        roomRuleService.save(roomRule);
    }
}
