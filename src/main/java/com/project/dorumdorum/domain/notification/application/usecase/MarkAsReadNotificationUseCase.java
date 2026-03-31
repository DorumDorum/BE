package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MarkAsReadNotificationUseCase {

    private final NotificationService notificationService;

    /**
     * 알림 읽음 처리
     * - 사용자 소유 알림인지 확인
     * - 해당 알림을 읽음 상태로 변경
     */
    @Transactional
    public void execute(String userNo, String notificationNo) {
        notificationService.markAsRead(notificationNo, userNo);
    }
}
