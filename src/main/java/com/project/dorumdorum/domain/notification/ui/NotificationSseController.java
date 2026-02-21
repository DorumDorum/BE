package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.domain.service.NotificationSseService;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class NotificationSseController {

    private final NotificationSseService notificationSseService;

    @Value("${notification.sse.timeout-ms:1800000}")
    private long timeoutMs;

    @GetMapping(value = "/api/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@CurrentUser String userId) throws IOException {
        return notificationSseService.connect(userId, timeoutMs);
    }
}
