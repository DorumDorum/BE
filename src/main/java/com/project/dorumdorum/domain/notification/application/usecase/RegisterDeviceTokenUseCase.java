package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.service.NotificationDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterDeviceTokenUseCase {

    private final NotificationDeviceService notificationDeviceService;

    /**
     * 푸시 알림 디바이스 토큰 등록 또는 갱신
     * - 사용자/디바이스 기준으로 기존 토큰을 확인
     * - 없으면 등록하고 있으면 최신 토큰으로 갱신
     */
    @Transactional
    public void execute(String userNo, String deviceId, String fcmToken) {
        notificationDeviceService.registerOrUpdateToken(userNo, deviceId, fcmToken);
    }
}
