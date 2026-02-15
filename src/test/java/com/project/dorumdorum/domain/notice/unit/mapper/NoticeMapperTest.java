package com.project.dorumdorum.domain.notice.unit.mapper;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.mapper.NoticeMapper;
import com.project.dorumdorum.domain.notice.application.mapper.NoticeMapperImpl;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NoticeMapper Unit Tests")
class NoticeMapperTest {

    private final NoticeMapper mapper = new NoticeMapperImpl();

    @Test
    @DisplayName("Should map notice entity to response")
    void toResponse_MapsFields() {
        Notice notice = Notice.builder()
                .noticeNo("n1")
                .title("title")
                .content("content")
                .writtenDate(LocalDate.of(2026, 2, 12))
                .originalLink("https://example.com")
                .build();

        NoticeResponse response = mapper.toResponse(notice);

        assertThat(response.noticeNo()).isEqualTo("n1");
        assertThat(response.title()).isEqualTo("title");
        assertThat(response.content()).isEqualTo("content");
        assertThat(response.writtenDate()).isEqualTo(LocalDate.of(2026, 2, 12));
        assertThat(response.originalLink()).isEqualTo("https://example.com");
    }

    @Test
    @DisplayName("Should map notice list to response list")
    void toResponseList_MapsAllItems() {
        List<Notice> notices = List.of(
                Notice.builder()
                        .noticeNo("n1")
                        .title("title1")
                        .content("content1")
                        .writtenDate(LocalDate.of(2026, 2, 12))
                        .originalLink("https://example.com/1")
                        .build(),
                Notice.builder()
                        .noticeNo("n2")
                        .title("title2")
                        .content("content2")
                        .writtenDate(LocalDate.of(2026, 2, 11))
                        .originalLink("https://example.com/2")
                        .build()
        );

        List<NoticeResponse> responses = mapper.toResponseList(notices);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).noticeNo()).isEqualTo("n1");
        assertThat(responses.get(1).noticeNo()).isEqualTo("n2");
    }

    @Test
    @DisplayName("Should return null when mapping null notice")
    void toResponse_WhenNull_ReturnsNull() {
        NoticeResponse response = mapper.toResponse(null);
        assertThat(response).isNull();
    }

    @Test
    @DisplayName("Should return null when mapping null list")
    void toResponseList_WhenNull_ReturnsNull() {
        List<NoticeResponse> responses = mapper.toResponseList(null);
        assertThat(responses).isNull();
    }
}
