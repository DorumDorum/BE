package com.project.dorumdorum.domain.notification.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "Notification", description = "알림 API")
public interface MarkAllAsReadNotificationsApiSpec {

    @Operation(
            summary = "모든 알림 읽음 처리",
            description = "현재 로그인한 사용자의 읽지 않은 알림을 모두 읽음 처리합니다."
    )
    @PatchMapping("/api/notifications/read")
    ResponseEntity<Void> markAllAsReadNotifications(
            @Parameter(hidden = true) String userNo
    );
}
