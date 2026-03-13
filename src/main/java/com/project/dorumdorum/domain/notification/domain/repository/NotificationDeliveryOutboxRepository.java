package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationDeliveryOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDeliveryOutboxRepository extends JpaRepository<NotificationDeliveryOutbox, String>, NotificationDeliveryOutboxQueryRepository {
}
