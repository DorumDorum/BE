package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.NotificationSettingRequest;
import com.project.dorumdorum.domain.notification.application.dto.response.NotificationSettingResponse;
import com.project.dorumdorum.domain.notification.application.usecase.LoadNotificationSettingUseCase;
import com.project.dorumdorum.domain.notification.application.usecase.UpdateNotificationSettingUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationSettingController 단위 테스트")
class NotificationSettingControllerTest {

    @Mock
    private LoadNotificationSettingUseCase loadUseCase;

    @Mock
    private UpdateNotificationSettingUseCase updateUseCase;

    @InjectMocks
    private NotificationSettingController controller;

    @Test
    @DisplayName("load는 알림 설정 조회 결과를 반환한다")
    void load_ReturnsSettings() {
        NotificationSettingResponse response = new NotificationSettingResponse(true, true, true, true, true, false);
        when(loadUseCase.execute("user-1")).thenReturn(response);

        ResponseEntity<NotificationSettingResponse> result = controller.load("user-1");

        verify(loadUseCase).execute("user-1");
        assertThat(result.getBody()).isEqualTo(response);
    }

    @Test
    @DisplayName("update는 알림 설정 저장 결과를 반환한다")
    void update_ReturnsSavedSettings() {
        NotificationSettingRequest request = new NotificationSettingRequest(false, true, true, false, true, false);
        NotificationSettingResponse response = new NotificationSettingResponse(false, true, true, false, true, false);
        when(updateUseCase.execute("user-1", request)).thenReturn(response);

        ResponseEntity<NotificationSettingResponse> result = controller.update("user-1", request);

        verify(updateUseCase).execute("user-1", request);
        assertThat(result.getBody()).isEqualTo(response);
    }
}
