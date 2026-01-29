package com.project.dorumdorum.domain.room.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;

import java.util.List;

/**
 * 내 방 규칙 조회 응답 DTO
 * RoomRule 도큐먼트 구조를 그대로 내려주기 위한 뷰 모델.
 */
public record MyRoomRuleResponse(
        String otherNotes,
        List<CategoryResponse> categories
) {

    public record CategoryResponse(
            RuleItemCategory category,
            List<RuleItemResponse> items
    ) {}

    public record RuleItemResponse(
            String label,
            RuleItemType itemType,
            String value,
            String extraValue,
            List<RuleOptionResponse> options
    ) {}

    public record RuleOptionResponse(
            String text,
            Boolean selected
    ) {}
}

