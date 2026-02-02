package com.project.dorumdorum.domain.user.application.dto.request;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateUserChecklistRequest(
        String otherNotes,
        @NotNull List<CreateCategoryRequest> categories
) {
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
