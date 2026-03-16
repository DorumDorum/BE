package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.usecase.RegisterDeviceTokenUseCase;
import com.project.dorumdorum.domain.notification.infra.sse.SseEmitterRegistry;
import com.project.dorumdorum.domain.notification.ui.spec.NotificationStreamApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationStreamController implements NotificationStreamApiSpec {

    private final SseEmitterRegistry sseEmitterRegistry;
    private final RegisterDeviceTokenUseCase registerDeviceTokenUseCase;

    @Override
    public SseEmitter stream(
            @CurrentUser String userNo,
            @RequestParam String deviceId
    ) {
        registerDeviceTokenUseCase.execute(userNo, deviceId, "");
        return sseEmitterRegistry.register(userNo, deviceId);
    }
}
