package com.project.dorumdorum.domain.notice.unit.response;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NoticeResponse Unit Tests")
class NoticeResponseTest {

    @Test
    @DisplayName("Should expose all response fields")
    void record_AccessorsReturnValues() {
        LocalDate writtenDate = LocalDate.of(2026, 2, 12);
        NoticeResponse response = new NoticeResponse(
                "n1",
                "Notice title",
                "Notice content",
                writtenDate,
                "https://example.com/notice/1"
        );

        assertThat(response.noticeNo()).isEqualTo("n1");
        assertThat(response.title()).isEqualTo("Notice title");
        assertThat(response.content()).isEqualTo("Notice content");
        assertThat(response.writtenDate()).isEqualTo(writtenDate);
        assertThat(response.originalLink()).isEqualTo("https://example.com/notice/1");
    }
}
