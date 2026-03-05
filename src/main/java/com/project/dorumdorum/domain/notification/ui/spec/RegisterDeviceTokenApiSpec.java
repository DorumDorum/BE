package com.project.dorumdorum.domain.notification.ui.spec;

import com.project.dorumdorum.domain.notification.application.dto.request.RegisterDeviceTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Notification", description = "알림 API")
public interface RegisterDeviceTokenApiSpec {

    @Operation(
            summary = "디바이스 토큰 등록",
            description = "현재 로그인한 사용자의 디바이스 FCM 토큰을 등록하거나 갱신합니다."
    )
    @PutMapping("/api/notifications/devices")
    ResponseEntity<Void> registerDeviceToken(
            @Parameter(hidden = true) String userNo,
            @RequestBody @Valid RegisterDeviceTokenRequest request
    );
}

