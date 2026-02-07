package com.project.dorumdorum.domain.notification.ui.spec;

import com.project.dorumdorum.domain.notification.application.dto.request.SendFirebaseTokenRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Notification")
public interface SendFirebaseTokenApiSpec {

    @Operation(
            summary = "FCM 토큰 등록 API",
            description = "로그인한 사용자가 자신의 FCM 디바이스 토큰을 서버에 등록합니다."
    )
    @PostMapping("/api/notification")
    BaseResponse<Void> sendFirebaseToken(
            @Parameter(hidden = true) String userNo,
            @RequestBody(
                    description = "FCM 디바이스 토큰",
                    required = true
            )
            SendFirebaseTokenRequest request
    );
}

