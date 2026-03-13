package com.project.dorumdorum.domain.notification.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "notification_delivery_outbox",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_outbox_device_delivery", columnNames = {"notification_outbox_no", "device_no"})
        }
)
public class NotificationDeliveryOutbox extends BaseEntity {

    @Id
    private String deliveryOutboxNo;

    @Column(name = "notification_outbox_no", nullable = false, updatable = false)
    private String notificationOutboxNo;

    @Column(name = "device_no", nullable = false, updatable = false)
    private String deviceNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationDeliveryOutboxStatus status;

    @Column(nullable = false)
    private int retryCount;

    private LocalDateTime nextRetryAt;

    private LocalDateTime processedAt;

    public static NotificationDeliveryOutbox createInit(String deliveryOutboxNo, String notificationOutboxNo, String deviceNo) {
        return NotificationDeliveryOutbox.builder()
                .deliveryOutboxNo(deliveryOutboxNo)
                .notificationOutboxNo(notificationOutboxNo)
                .deviceNo(deviceNo)
                .status(NotificationDeliveryOutboxStatus.INIT)
                .retryCount(0)
                .build();
    }

    public void success() {
        this.status = NotificationDeliveryOutboxStatus.SUCCESS;
        this.nextRetryAt = null;
        this.processedAt = LocalDateTime.now();
    }

    public void failWithBackoff(int maxRetryCount, long baseBackoffSeconds) {
        this.retryCount += 1;

        if (this.retryCount >= maxRetryCount) {
            this.status = NotificationDeliveryOutboxStatus.FAIL;
            this.nextRetryAt = null;
            this.processedAt = LocalDateTime.now();
            return;
        }

        long multiplier = 1L << Math.max(0, this.retryCount - 1);   // Exponential Backoff
        long delaySeconds = baseBackoffSeconds * multiplier;

        this.status = NotificationDeliveryOutboxStatus.INIT;
        this.nextRetryAt = LocalDateTime.now().plusSeconds(delaySeconds);
    }
}
