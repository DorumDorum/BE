package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification save(String recipientNo, String title, String body, NotificationType type, String relatedId) {
        Notification notification = Notification.builder()
                .recipientNo(recipientNo)
                .title(title)
                .body(body)
                .type(type)
                .relatedId(relatedId)
                .build();

        return notificationRepository.save(notification);
    }

    public List<Notification> searchByCursor(String recipientNo, java.time.LocalDateTime cursorCreatedAt, String cursorId, int limitPlusOne) {
        return notificationRepository.findByCursor(recipientNo, cursorCreatedAt, cursorId, limitPlusOne);
    }

    public void markAsRead(String notificationNo, String userNo) {
        Notification notification = notificationRepository.findByNotificationNoAndRecipientNo(notificationNo, userNo)
                .orElseThrow(() -> new RestApiException(NOTIFICATION_NOT_FOUND));

        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
