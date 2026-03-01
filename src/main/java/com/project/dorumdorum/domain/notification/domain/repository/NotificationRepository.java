package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationRepositoryCustom {

    Optional<Notification> findByNotificationNoAndRecipientNo(String notificationNo, String recipientNo);
}
