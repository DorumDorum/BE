package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarkAsReadNotificationUseCase {

    private final NotificationService notificationService;

    @Transactional
    public void execute(String userNo, String notificationNo) {
        notificationService.read(notificationNo, userNo);
    }
}
