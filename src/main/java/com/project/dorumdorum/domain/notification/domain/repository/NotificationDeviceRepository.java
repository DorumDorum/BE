package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationDeviceRepository extends JpaRepository<Device, String> {

    Optional<Device> findByUserNoAndDeviceId(String userNo, String deviceId);

    List<Device> findByUserNo(String userNo);

    List<Device> findByFcmTokenIn(List<String> fcmTokens);
}

