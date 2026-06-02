package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.MarkAllAsReadNotificationsUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.MarkAllAsReadNotificationsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MarkAllAsReadNotificationsController implements MarkAllAsReadNotificationsApiSpec {

    private final MarkAllAsReadNotificationsUseCase markAllAsReadNotificationsUseCase;

    @Override
    public ResponseEntity<Void> markAllAsReadNotifications(@CurrentUser String userNo) {
        markAllAsReadNotificationsUseCase.execute(userNo);
        return ResponseEntity.noContent().build();
    }
}
