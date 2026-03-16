package com.project.dorumdorum.domain.notification.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "notification_outbox")
public class NotificationOutbox extends BaseEntity {

    @Id
    private String outboxNo;

    @Column(nullable = false)
    private String recipientNo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    private String relatedId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationOutboxStatus status;

    private String notificationNo;

    @Column(nullable = false)
    private int retryCount;

    private LocalDateTime nextRetryAt;

    private LocalDateTime processedAt;

    public static NotificationOutbox createInit(
            String outboxNo,
            String recipientNo,
            String title,
            String body,
            NotificationType type,
            String relatedId
    ) {
        return NotificationOutbox.builder()
                .outboxNo(outboxNo)
                .recipientNo(recipientNo)
                .title(title)
                .body(body)
                .type(type)
                .relatedId(relatedId)
                .status(NotificationOutboxStatus.INIT)
                .retryCount(0)
                .build();
    }

    public void success(String notificationNo) {
        this.status = NotificationOutboxStatus.SUCCESS;
        this.notificationNo = notificationNo;
        this.nextRetryAt = null;
        this.processedAt = LocalDateTime.now();
    }

    public void failWithBackoff(int maxRetryCount, long baseBackoffSeconds) {
        this.retryCount += 1;

        if (this.retryCount >= maxRetryCount) {
            this.status = NotificationOutboxStatus.FAIL;
            this.nextRetryAt = null;
            this.processedAt = LocalDateTime.now();
            return;
        }

        long multiplier = 1L << Math.max(0, this.retryCount - 1);
        long delaySeconds = baseBackoffSeconds * multiplier;

        this.status = NotificationOutboxStatus.INIT;
        this.nextRetryAt = LocalDateTime.now().plusSeconds(delaySeconds);
    }
}
