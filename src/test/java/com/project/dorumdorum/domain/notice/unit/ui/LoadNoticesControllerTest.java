package com.project.dorumdorum.domain.notice.unit.ui;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticesUseCase;
import com.project.dorumdorum.domain.notice.ui.LoadNoticesController;
import com.project.dorumdorum.global.common.BaseResponse;
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
@DisplayName("LoadNoticesController Unit Tests")
class LoadNoticesControllerTest {

    @Mock
    private LoadNoticesUseCase loadNoticesUseCase;

    @InjectMocks
    private LoadNoticesController controller;

    @Test
    @DisplayName("Should return success response with notices")
    void loadNotices_ReturnsUseCaseResult() {
        List<NoticeResponse> payload = List.of(
                new NoticeResponse("n1", "title", "content", LocalDate.of(2026, 2, 12), "https://example.com")
        );
        when(loadNoticesUseCase.execute()).thenReturn(payload);

        BaseResponse<List<NoticeResponse>> response = controller.loadNotices();

        verify(loadNoticesUseCase).execute();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getResult()).isEqualTo(payload);
    }
}
