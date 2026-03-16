package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.service.NotificationDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterDeviceTokenUseCase {

    private final NotificationDeviceService notificationDeviceService;

    @Transactional
    public void execute(String userNo, String deviceId, String fcmToken) {
        notificationDeviceService.registerOrUpdateToken(userNo, deviceId, fcmToken);
    }
}
