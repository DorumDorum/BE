package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.response.LoadNotificationResponse;
import com.project.dorumdorum.domain.notification.application.usecase.LoadMyNotificationsUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.LoadMyNotificationsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.pagination.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyNotificationsController implements LoadMyNotificationsApiSpec {

    private final LoadMyNotificationsUseCase loadMyNotificationsUseCase;

    @Override
    public ResponseEntity<CursorPage<LoadNotificationResponse>> loadMyNotifications(
            @CurrentUser String userNo,
            @RequestParam(required = false) String cursor
    ) {
        return ResponseEntity.ok(loadMyNotificationsUseCase.execute(userNo, cursor));
    }
}
