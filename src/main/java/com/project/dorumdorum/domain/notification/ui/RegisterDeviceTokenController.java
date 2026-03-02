package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.RegisterDeviceTokenRequest;
import com.project.dorumdorum.domain.notification.application.usecase.RegisterDeviceTokenUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class RegisterDeviceTokenController {

    private final RegisterDeviceTokenUseCase registerDeviceTokenUseCase;

    @PutMapping("/devices")
    public ResponseEntity<Void> registerDeviceToken(
            @CurrentUser String userNo,
            @RequestBody @Valid RegisterDeviceTokenRequest request
    ) {
        registerDeviceTokenUseCase.execute(userNo, request.deviceId(), request.fcmToken());
        return ResponseEntity.ok().build();
    }
}
