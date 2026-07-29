package com.project.dorumdorum.domain.support.ui;

import com.project.dorumdorum.domain.support.application.dto.request.CreateSupportInquiryRequest;
import com.project.dorumdorum.domain.support.application.dto.response.SupportInquiryResponse;
import com.project.dorumdorum.domain.support.application.usecase.CreateSupportInquiryUseCase;
import com.project.dorumdorum.domain.support.application.usecase.LoadMySupportInquiriesUseCase;
import com.project.dorumdorum.domain.support.application.usecase.LoadSupportInquiryDetailUseCase;
import com.project.dorumdorum.domain.support.domain.entity.SupportInquiryCategory;
import com.project.dorumdorum.domain.support.domain.entity.SupportInquiryStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SupportInquiryController 단위 테스트")
class SupportInquiryControllerTest {

    @Mock
    private CreateSupportInquiryUseCase createUseCase;

    @Mock
    private LoadMySupportInquiriesUseCase loadAllUseCase;

    @Mock
    private LoadSupportInquiryDetailUseCase loadDetailUseCase;

    @InjectMocks
    private SupportInquiryController controller;

    @Test
    @DisplayName("create는 문의 접수 결과를 반환한다")
    void create_ReturnsCreatedInquiry() {
        CreateSupportInquiryRequest request = new CreateSupportInquiryRequest(SupportInquiryCategory.APP_USAGE, "문의합니다");
        SupportInquiryResponse response = new SupportInquiryResponse("1", SupportInquiryCategory.APP_USAGE, "문의합니다", SupportInquiryStatus.RECEIVED, LocalDateTime.now());
        when(createUseCase.execute("user-1", request)).thenReturn(response);

        ResponseEntity<SupportInquiryResponse> result = controller.create("user-1", request);

        verify(createUseCase).execute("user-1", request);
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("loadAll은 내 문의 목록을 반환한다")
    void loadAll_ReturnsMyInquiries() {
        List<SupportInquiryResponse> responses = List.of();
        when(loadAllUseCase.execute("user-1")).thenReturn(responses);

        ResponseEntity<List<SupportInquiryResponse>> result = controller.loadAll("user-1");

        verify(loadAllUseCase).execute("user-1");
        assertThat(result.getBody()).isEqualTo(responses);
    }
}
