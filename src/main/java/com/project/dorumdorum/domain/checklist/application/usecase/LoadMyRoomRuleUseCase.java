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

    /**
     * 방 체크리스트 규칙 조회
     * - 방 번호로 규칙 정보를 조회
     * - 응답 DTO로 변환해 반환
     */
    public MyRoomRuleResponse execute(String roomNo) {
        RoomRule roomRule = roomRuleService.findByRoomNo(roomNo);
        return roomRuleMapper.toResponse(roomRule);
    }
}
