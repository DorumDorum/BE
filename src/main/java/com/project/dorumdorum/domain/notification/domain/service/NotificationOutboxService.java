package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationType;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationOutboxRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationOutboxService {

    private static final int MAX_RETRY_COUNT = 5;
    private static final long BASE_BACKOFF_SECONDS = 5L;

    private final NotificationOutboxRepository notificationOutboxRepository;

    @Transactional
    public NotificationOutbox saveInit(
            String outboxNo,
            String recipientNo,
            String title,
            String body,
            NotificationType type,
            String relatedId
    ) {
        NotificationOutbox outbox = NotificationOutbox.createInit(outboxNo, recipientNo, title, body, type, relatedId);
        return notificationOutboxRepository.save(outbox);
    }

    @Transactional(readOnly = true)
    public List<NotificationOutbox> loadRetryBatch(int size) {
        return notificationOutboxRepository.findRetryableByStatus(
                NotificationOutboxStatus.INIT,
                LocalDateTime.now(),
                PageRequest.of(0, size)
        );
    }

    @Transactional
    public NotificationOutbox success(String outboxNo, String notificationNo) {
        NotificationOutbox outbox = findById(outboxNo);
        outbox.success(notificationNo);
        return outbox;
    }

    @Transactional
    public NotificationOutbox fail(String outboxNo) {
        NotificationOutbox outbox = findById(outboxNo);
        outbox.failWithBackoff(MAX_RETRY_COUNT, BASE_BACKOFF_SECONDS);
        return outbox;
    }

    private NotificationOutbox findById(String outboxNo) {
        return notificationOutboxRepository.findById(outboxNo)
                .orElseThrow(() -> new RestApiException(NOTIFICATION_NOT_FOUND));
    }
}
