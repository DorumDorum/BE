package com.project.dorumdorum.domain.notification.ui.spec;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notification", description = "알림 API")
public interface LoadMyNotificationsApiSpec {

    @Operation(
            summary = "내 알림 목록 조회",
            description = "현재 로그인한 사용자의 알림 목록을 최신순으로 커서 기반 조회합니다."
    )
    @GetMapping("/api/notifications")
    ResponseEntity<CursorPage<LoadNotificationResponse>> loadMyNotifications(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "다음 페이지 커서") @RequestParam(required = false) String cursor
    );
}
