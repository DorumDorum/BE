package com.project.dorumdorum.domain.user.domain.entity;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "user_checklists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserChecklist {

    @Id
    private String id;

    private String userNo;  // User 엔티티와 연결

    @Builder.Default
    private List<CategoryData> categories = new ArrayList<>();

    private String otherNotes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryData {
        private RuleItemCategory category;  // BASIC_INFO, LIFESTYLE_PATTERN, ADDITIONAL_RULES

        @Builder.Default
        private List<RuleItemData> items = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RuleItemData {
        private String label;
        private RuleItemType itemType;
        private String value;
        private String extraValue;

        @Builder.Default
        private List<RuleOptionData> options = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RuleOptionData {
        private String text;
        private Boolean selected;
    }

    public void updateOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    public void updateCategories(List<CategoryData> categories) {
        this.categories = categories;
    }
}
