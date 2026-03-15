package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationDeviceService {

    private final NotificationDeviceRepository notificationDeviceRepository;

    @Transactional
    public void registerOrUpdateToken(String userNo, String deviceId, String fcmToken) {
        notificationDeviceRepository.findByUserNoAndDeviceId(userNo, deviceId)
                .ifPresentOrElse(
                        device -> {
                            if (fcmToken != null && !fcmToken.isBlank()) {
                                device.updateFcmToken(fcmToken);
                            }
                        },
                        () -> notificationDeviceRepository.save(
                                Device.builder()
                                        .userNo(userNo)
                                        .deviceId(deviceId)
                                        .fcmToken(fcmToken != null ? fcmToken : "")
                                        .build()
                        )
                );
    }
}
