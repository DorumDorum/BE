package com.project.dorumdorum.domain.notification.ui.spec;

import com.project.dorumdorum.domain.notification.application.dto.request.NotificationSettingRequest;
import com.project.dorumdorum.domain.notification.application.dto.response.NotificationSettingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Notification")
public interface NotificationSettingApiSpec {

    @Operation(summary = "내 알림 설정 조회 API", description = "사용자별 알림 수신 설정을 조회합니다. 저장된 설정이 없으면 기본값을 반환합니다.")
    @GetMapping("/api/users/me/notification-settings")
    ResponseEntity<NotificationSettingResponse> load(@Parameter(hidden = true) String userNo);

    @Operation(summary = "내 알림 설정 저장 API", description = "사용자별 알림 수신 설정을 저장합니다.")
    @PutMapping("/api/users/me/notification-settings")
    ResponseEntity<NotificationSettingResponse> update(
            @Parameter(hidden = true) String userNo,
            @RequestBody NotificationSettingRequest request
    );
}
