package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.Device;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationDeviceService {

    private final NotificationDeviceRepository notificationDeviceRepository;

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

    public List<Device> findByUserNo(String userNo) {
        return notificationDeviceRepository.findByUserNo(userNo);
    }

    public List<Device> findByFcmTokens(List<String> fcmTokens) {
        List<String> sanitizedTokens = sanitizeTokens(fcmTokens);
        if (sanitizedTokens.isEmpty()) {
            return List.of();
        }
        return notificationDeviceRepository.findByFcmTokenIn(sanitizedTokens);
    }

    public void clearInvalidFcmTokens(List<String> fcmTokens) {
        List<String> sanitizedTokens = sanitizeTokens(fcmTokens);
        if (sanitizedTokens.isEmpty()) {
            return;
        }

        List<Device> devices = notificationDeviceRepository.findByFcmTokenIn(sanitizedTokens);

        for (Device device : devices) {
            device.clearFcmToken();
        }
        notificationDeviceRepository.saveAll(devices);
    }

    private static List<String> sanitizeTokens(List<String> fcmTokens) {
        if (fcmTokens == null || fcmTokens.isEmpty()) {
            return List.of();
        }
        return fcmTokens.stream()
                .filter(token -> token != null && !token.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }
}
