package com.project.dorumdorum.domain.notification.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Notification", description = "알림 API")
public interface MarkAsReadNotificationApiSpec {

    @Operation(
            summary = "알림 읽음 처리",
            description = "알림을 눌렀을 때 해당 알림을 읽음 처리합니다. 본인의 알림만 처리할 수 있습니다."
    )
    @PatchMapping("/api/notifications/{notificationNo}/read")
    ResponseEntity<Void> markAsReadNotification(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "알림 번호") @PathVariable String notificationNo
    );
}
