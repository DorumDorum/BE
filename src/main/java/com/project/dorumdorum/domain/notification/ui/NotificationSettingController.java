package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.NotificationSettingRequest;
import com.project.dorumdorum.domain.notification.application.dto.response.NotificationSettingResponse;
import com.project.dorumdorum.domain.notification.application.usecase.LoadNotificationSettingUseCase;
import com.project.dorumdorum.domain.notification.application.usecase.UpdateNotificationSettingUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.NotificationSettingApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationSettingController implements NotificationSettingApiSpec {

    private final LoadNotificationSettingUseCase loadNotificationSettingUseCase;
    private final UpdateNotificationSettingUseCase updateNotificationSettingUseCase;

    @Override
    public ResponseEntity<NotificationSettingResponse> load(@CurrentUser String userNo) {
        return ResponseEntity.ok(loadNotificationSettingUseCase.execute(userNo));
    }

    @Override
    public ResponseEntity<NotificationSettingResponse> update(
            @CurrentUser String userNo,
            @RequestBody NotificationSettingRequest request
    ) {
        return ResponseEntity.ok(updateNotificationSettingUseCase.execute(userNo, request));
    }
}
