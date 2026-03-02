package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepositoryCustom {

    List<Notification> searchByCursor(String recipientNo, LocalDateTime cursorCreatedAt, String cursorId, int limitPlusOne);
}
