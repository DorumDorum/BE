package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.infra.sse.SseEmitterRegistry;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.EMPTY_JWT;
import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_ACCESS_TOKEN;
import static com.project.dorumdorum.global.exception.code.status.NotificationErrorStatus.DEVICE_ID_REQUIRED;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationStreamController {

    private final SseEmitterRegistry sseEmitterRegistry;
    private final TokenProvider tokenProvider;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(HttpServletRequest request) {
        String token = request.getParameter("accessToken");
        if (token == null || token.isBlank())
            throw new RestApiException(EMPTY_JWT);
        if (!tokenProvider.validateToken(token))
            throw new RestApiException(INVALID_ACCESS_TOKEN);

        String userNo = tokenProvider.getId(token)
                .orElseThrow(() -> new RestApiException(INVALID_ACCESS_TOKEN));
        String deviceId = request.getParameter("deviceId");
        if (deviceId == null || deviceId.isBlank())
            throw new RestApiException(DEVICE_ID_REQUIRED);

        return sseEmitterRegistry.register(userNo, deviceId);
    }
}
