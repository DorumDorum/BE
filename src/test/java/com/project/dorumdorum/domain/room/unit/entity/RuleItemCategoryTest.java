package com.project.dorumdorum.domain.room.unit.entity;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RuleItemCategory Enum Unit Tests")
class RuleItemCategoryTest {

    @Test
    @DisplayName("Should expose all expected enum values")
    void values_ShouldMatchExpectedOrder() {
        RuleItemCategory[] values = RuleItemCategory.values();

        assertThat(values).containsExactly(
                RuleItemCategory.BASIC_INFO,
                RuleItemCategory.LIFESTYLE_PATTERN,
                RuleItemCategory.ADDITIONAL_RULES
        );
        assertThat(RuleItemCategory.valueOf("BASIC_INFO")).isEqualTo(RuleItemCategory.BASIC_INFO);
    }
}
