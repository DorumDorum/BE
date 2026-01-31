package com.project.dorumdorum.domain.room.application.dto.request;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateRoomRuleRequest(
        String otherNotes,
        @NotNull List<UpdateCategoryRequest> categories
) {
    public record UpdateCategoryRequest(
            RuleItemCategory category,
            List<UpdateRuleItemRequest> items
    ) {}

    public record UpdateRuleItemRequest(
            String label,
            RuleItemType itemType,
            String value,
            String extraValue,
            List<UpdateRuleOptionRequest> options
    ) {}

    public record UpdateRuleOptionRequest(
            String text,
            Boolean selected
    ) {}
}
