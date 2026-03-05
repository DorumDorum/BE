package com.project.dorumdorum.domain.notification.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notification", description = "알림 API")
public interface NotificationStreamApiSpec {

    @Operation(
            summary = "알림 SSE 스트림 연결",
            description = "현재 로그인한 사용자의 알림을 SSE(Server-Sent Events)로 실시간 수신하기 위한 스트림을 연다."
    )
    @GetMapping(value = "/api/notifications/stream", produces = "text/event-stream")
    SseEmitter stream(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "디바이스 식별자") @RequestParam("deviceId") String deviceId
    );
}

