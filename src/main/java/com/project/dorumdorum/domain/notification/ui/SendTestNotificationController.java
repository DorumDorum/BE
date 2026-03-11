package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.SendTestNotificationUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.SendTestNotificationApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendTestNotificationController implements SendTestNotificationApiSpec {

    private final SendTestNotificationUseCase sendTestNotificationUseCase;

    @Override
    public ResponseEntity<Void> sendTestNotification(@RequestParam String receiverNo) {
        sendTestNotificationUseCase.execute(receiverNo);
        return ResponseEntity.noContent().build();
    }
}
