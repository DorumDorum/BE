package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.ReadNotificationUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.ReadNotificationApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadNotificationController implements ReadNotificationApiSpec {

    private final ReadNotificationUseCase readNotificationUseCase;

    @Override
    public ResponseEntity<Void> readNotification(
            @CurrentUser String userNo,
            @PathVariable String notificationNo
    ) {
        readNotificationUseCase.execute(userNo, notificationNo);
        return ResponseEntity.noContent().build();
    }
}
