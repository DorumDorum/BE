package com.project.dorumdorum.domain.room.application.dto.request;

import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RoomCreateRequest(
        @NotNull RoomType roomType,
        @NotNull Integer capacity,
        @NotBlank String title,
        @NotNull CreateRoomRuleRequest rule
) {
    public record CreateRoomRuleRequest(
            String otherNotes,
            List<CreateCategoryRequest> categories
    ) {}

    public record CreateCategoryRequest(
            RuleItemCategory category,
            List<CreateRuleItemRequest> items
    ) {}

    public record CreateRuleItemRequest(
            String label,
            RuleItemType itemType,
            String value,
            String extraValue,
            List<CreateRuleOptionRequest> options
    ) {}

    public record CreateRuleOptionRequest(
            String text,
            Boolean selected
    ) {}
}
