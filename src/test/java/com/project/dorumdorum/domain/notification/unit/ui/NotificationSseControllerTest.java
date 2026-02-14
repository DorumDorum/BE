package com.project.dorumdorum.domain.notification.unit.ui;

import com.project.dorumdorum.domain.notification.application.NotificationSseService;
import com.project.dorumdorum.domain.notification.ui.NotificationSseController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationSseController Unit Tests")
class NotificationSseControllerTest {

    @Mock
    private NotificationSseService notificationSseService;

    @InjectMocks
    private NotificationSseController controller;

    @Test
    @DisplayName("Should connect stream with configured timeout")
    void stream_DelegatesToService() throws Exception {
        ReflectionTestUtils.setField(controller, "timeoutMs", 1234L);
        SseEmitter emitter = new SseEmitter(1234L);
        when(notificationSseService.connect("token", 1234L)).thenReturn(emitter);

        SseEmitter result = controller.stream("token");

        assertThat(result).isEqualTo(emitter);
        verify(notificationSseService).connect("token", 1234L);
    }
}
