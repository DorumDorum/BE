package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutboxStatus;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutboxStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationDeliveryOutboxQueryRepository {

    List<NotificationDeliveryOutbox> findProcessableByStatus(
            NotificationDeliveryOutboxStatus status,
            NotificationOutboxStatus notificationOutboxStatus,
            LocalDateTime now,
            Pageable pageable
    );
}
