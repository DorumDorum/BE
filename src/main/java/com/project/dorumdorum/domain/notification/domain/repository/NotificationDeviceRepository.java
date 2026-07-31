package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationDeviceRepository extends JpaRepository<Device, String> {

    Optional<Device> findByUserNoAndDeviceId(String userNo, String deviceId);

    List<Device> findByUserNo(String userNo);

    List<Device> findByFcmTokenIn(List<String> fcmTokens);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Device d SET d.deletedAt = CURRENT_TIMESTAMP WHERE d.userNo = :userNo AND d.deletedAt IS NULL")
    void deleteAllByUserNo(@Param("userNo") String userNo);
}
