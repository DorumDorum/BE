package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.MarkAsReadNotificationUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.MarkAsReadNotificationApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MarkAsReadNotificationController implements MarkAsReadNotificationApiSpec {

    private final MarkAsReadNotificationUseCase markAsReadNotificationUseCase;

    @Override
    public ResponseEntity<Void> markAsReadNotification(
            @CurrentUser String userNo,
            @PathVariable String notificationNo
    ) {
        markAsReadNotificationUseCase.execute(userNo, notificationNo);
        return ResponseEntity.noContent().build();
    }
}
