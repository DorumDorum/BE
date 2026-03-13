package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutbox;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationOutboxStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationOutboxQueryRepository {

    List<NotificationOutbox> findRetryableByStatus(
            NotificationOutboxStatus status,
            LocalDateTime now,
            Pageable pageable
    );
}
