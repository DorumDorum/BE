package com.project.dorumdorum.domain.room.application.usecase;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.room.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.room.domain.entity.RoomRule;
import com.project.dorumdorum.domain.room.domain.service.RoomRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMyRoomRuleUseCase {

    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    public MyRoomRuleResponse execute(Long roomNo) {
        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);
        return roomRuleMapper.toResponse(roomRule);
    }
}
