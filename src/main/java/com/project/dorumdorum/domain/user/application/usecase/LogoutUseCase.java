package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.chat.presence.PresenceService;
import com.project.dorumdorum.domain.notification.domain.service.NotificationService;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import com.project.dorumdorum.domain.user.domain.service.TokenBlacklistService;
import com.project.dorumdorum.domain.user.domain.service.TokenWhitelistService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutUseCase {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final TokenWhitelistService tokenWhitelistService;
    private final PresenceService presenceService;
    private final NotificationService notificationService;

    public void execute(String accessToken) {
        String userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));

        Duration expiration = tokenProvider.getRemainingDuration(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ACCESS_TOKEN));

        tokenWhitelistService.deleteWhitelistToken(accessToken);
        refreshTokenService.deleteRefreshToken(userNo);
        tokenBlacklistService.blacklist(accessToken, expiration);
        notificationService.clearToken(userNo);
        presenceService.clear(userNo);
    }
}
