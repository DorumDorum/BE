package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, String>, NotificationQueryRepository {

    Optional<Notification> findByNotificationNoAndRecipientNo(String notificationNo, String recipientNo);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notification n SET n.deletedAt = CURRENT_TIMESTAMP WHERE n.recipientNo = :userNo AND n.deletedAt IS NULL")
    void deleteAllByRecipientNo(@Param("userNo") String userNo);
}
