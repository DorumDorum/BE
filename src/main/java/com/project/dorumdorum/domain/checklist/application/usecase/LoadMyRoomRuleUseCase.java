package com.project.dorumdorum.domain.checklist.application.usecase;

import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.checklist.application.mapper.RoomRuleMapper;
import com.project.dorumdorum.domain.checklist.domain.entity.RoomRule;
import com.project.dorumdorum.domain.checklist.domain.service.RoomRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadMyRoomRuleUseCase {

    private final RoomRuleService roomRuleService;
    private final RoomRuleMapper roomRuleMapper;

    public MyRoomRuleResponse execute(String roomNo) {
        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);
        return roomRuleMapper.toResponse(roomRule);
    }
}
