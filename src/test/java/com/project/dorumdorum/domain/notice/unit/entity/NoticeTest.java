package com.project.dorumdorum.domain.notice.unit.entity;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Notice Entity Unit Tests")
class NoticeTest {

    @Test
    @DisplayName("Should build notice with all fields")
    void builder_SetsAllFields() {
        LocalDate writtenDate = LocalDate.of(2026, 2, 12);
        Notice notice = Notice.builder()
                .noticeNo("n1")
                .title("Notice title")
                .content("Notice content")
                .writtenDate(writtenDate)
                .originalLink("https://example.com/notice/1")
                .build();

        assertThat(notice.getNoticeNo()).isEqualTo("n1");
        assertThat(notice.getTitle()).isEqualTo("Notice title");
        assertThat(notice.getContent()).isEqualTo("Notice content");
        assertThat(notice.getWrittenDate()).isEqualTo(writtenDate);
        assertThat(notice.getOriginalLink()).isEqualTo("https://example.com/notice/1");
    }
}
