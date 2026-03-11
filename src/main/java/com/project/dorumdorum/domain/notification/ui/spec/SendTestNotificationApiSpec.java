package com.project.dorumdorum.domain.notification.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notification", description = "알림 API")
public interface SendTestNotificationApiSpec {

    @Operation(
            summary = "테스트 알림 전송",
            description = "테스트 목적으로 receiverNo를 받아 해당 유저에게 알림 전송 이벤트를 발행합니다."
    )
    @PostMapping("/api/notifications/test/send")
    ResponseEntity<Void> sendTestNotification(
            @Parameter(description = "수신자 유저 번호") @RequestParam String receiverNo
    );
}
