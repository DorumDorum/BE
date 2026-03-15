package com.project.dorumdorum.domain.notice.unit.usecase;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.mapper.NoticeMapper;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticesUseCase;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.service.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadNoticesUseCase Unit Tests")
class LoadNoticesUseCaseTest {

    @Mock
    private NoticeService noticeService;
    @Mock
    private NoticeMapper noticeMapper;

    @InjectMocks
    private LoadNoticesUseCase useCase;

    @Test
    @DisplayName("Should load notices and map to response list")
    void execute_ReturnsMappedResponses() {
        List<Notice> notices = List.of(
                Notice.builder()
                        .noticeNo("n1")
                        .title("title")
                        .content("content")
                        .writtenDate(LocalDate.of(2026, 2, 12))
                        .originalLink("https://example.com")
                        .build()
        );
        List<NoticeResponse> mapped = List.of(
                new NoticeResponse("n1", "title", "content", LocalDate.of(2026, 2, 12), "https://example.com")
        );
        when(noticeService.loadAllByWrittenDateDesc()).thenReturn(notices);
        when(noticeMapper.toResponseList(notices)).thenReturn(mapped);

        List<NoticeResponse> result = useCase.execute();

        assertThat(result).isEqualTo(mapped);
        verify(noticeService).loadAllByWrittenDateDesc();
        verify(noticeMapper).toResponseList(notices);
    }
}
