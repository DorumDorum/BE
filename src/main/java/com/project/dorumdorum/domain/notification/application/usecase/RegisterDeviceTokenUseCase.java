package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.domain.repository.UserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterDeviceTokenUseCase {

    private final UserDeviceTokenRepository userDeviceTokenRepository;

    public void execute(String userNo, String deviceId, String fcmToken) {
        userDeviceTokenRepository.save(userNo, deviceId, fcmToken);
    }
}
