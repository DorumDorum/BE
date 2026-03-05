package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationQueryRepository {

    List<Notification> findByCursor(String recipientNo, LocalDateTime cursorCreatedAt, String cursorId, int limitPlusOne);
}
