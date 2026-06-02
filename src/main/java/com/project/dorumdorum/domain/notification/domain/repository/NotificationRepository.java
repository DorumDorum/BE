package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationQueryRepository {

    Optional<Notification> findByNotificationNoAndRecipientNo(String notificationNo, String recipientNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Notification notification
            set notification.readAt = :readAt
            where notification.recipientNo = :recipientNo
              and notification.readAt is null
              and notification.deletedAt is null
            """)
    int markAllAsReadByRecipientNo(
            @Param("recipientNo") String recipientNo,
            @Param("readAt") LocalDateTime readAt
    );
}
