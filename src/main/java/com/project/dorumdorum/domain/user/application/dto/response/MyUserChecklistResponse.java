package com.project.dorumdorum.domain.user.application.dto.response;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;
import lombok.Builder;

import java.util.List;

@Builder
public record MyUserChecklistResponse(
        String otherNotes,
        List<CategoryResponse> categories
) {
    @Builder
    public record CategoryResponse(
            RuleItemCategory category,
            List<RuleItemResponse> items
    ) {}

    @Builder
    public record RuleItemResponse(
            String label,
            RuleItemType itemType,
            String value,
            String extraValue,
            List<RuleOptionResponse> options
    ) {}

    @Builder
    public record RuleOptionResponse(
            String text,
            Boolean selected
    ) {}
}
