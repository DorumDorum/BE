package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.room.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.service.RoomRuleService;
import com.project.dorumdorum.domain.room.domain.service.RoommateService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus.NO_PERMISSION_ON_ROOM;

@Service
@RequiredArgsConstructor
public class LoadMyRoomRuleUseCase {

    private final RoommateService roommateService;
    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    public MyRoomRuleResponse execute(Long userNo, Long roomNo) {
        if (!roommateService.isUserRoommate(userNo, roomNo))
            throw new RestApiException(NO_PERMISSION_ON_ROOM);

        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);
        return roomRuleMapper.toResponse(roomRule);
    }
}
