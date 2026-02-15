package com.project.dorumdorum.domain.room.unit.entity;

import com.project.dorumdorum.domain.room.domain.entity.RuleItemType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RuleItemType Enum Unit Tests")
class RuleItemTypeTest {

    @Test
    @DisplayName("Should expose value and option types")
    void values_ShouldContainValueAndOption() {
        assertThat(RuleItemType.values()).containsExactly(RuleItemType.VALUE, RuleItemType.OPTION);
        assertThat(RuleItemType.valueOf("OPTION")).isEqualTo(RuleItemType.OPTION);
    }
}
