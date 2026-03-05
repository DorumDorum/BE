package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.RegisterDeviceTokenRequest;
import com.project.dorumdorum.domain.notification.application.usecase.RegisterDeviceTokenUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.RegisterDeviceTokenApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterDeviceTokenController implements RegisterDeviceTokenApiSpec {

    private final RegisterDeviceTokenUseCase registerDeviceTokenUseCase;

    @Override
    public ResponseEntity<Void> registerDeviceToken(
            @CurrentUser String userNo,
            @RequestBody @Valid RegisterDeviceTokenRequest request
    ) {
        registerDeviceTokenUseCase.execute(userNo, request.deviceId(), request.fcmToken());
        return ResponseEntity.ok().build();
    }
}
