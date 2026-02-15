package com.project.dorumdorum.domain.room.unit.entity;

import com.project.dorumdorum.domain.room.domain.entity.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tag Enum Unit Tests")
class TagTest {

    @Test
    @DisplayName("Should expose expected Korean tag names")
    void values_ShouldContainAllTags() {
        assertThat(Tag.values()).containsExactly(
                Tag.운동,
                Tag.비흡연,
                Tag.조용한,
                Tag.새벽형,
                Tag.아침형,
                Tag.갓생,
                Tag.늦잠
        );
        assertThat(Tag.valueOf("운동")).isEqualTo(Tag.운동);
    }
}
