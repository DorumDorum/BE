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

import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.INVALID_ROOM_CAPACITY;
import static com.project.dorumdorum.global.exception.code.status.RoomErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRoomRuleUseCase {

    private final RoomService roomService;
    private final RoommateService roommateService;
    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    /**
     * 방 규칙 및 모집 조건 수정
     * - 방장을 검증하고 방/규칙 정보를 조회
     * - 방 기본 정보와 방 규칙을 함께 갱신
     * - 변경된 규칙을 저장
     */
    public void execute(String userNo, String roomNo, UpdateRoomRuleRequest request) {
        Room room = roomService.findByIdForUpdate(roomNo);

        if (!roommateService.isHost(userNo, room))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);

        if (!room.isValidCapacity(request.capacity())) {
            throw new RestApiException(INVALID_ROOM_CAPACITY);
        }
        room.updateCapacity(request.capacity());
        room.updateRoomType(request.roomType());
        room.updateResidencePeriod(request.residencePeriod());

        roomRuleMapper.updateRoomRule(request, roomRule);
        roomRuleService.save(roomRule);
    }
}
