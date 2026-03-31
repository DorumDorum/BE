package com.project.dorumdorum.domain.user.application.usecase;

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

    /**
     * 로그아웃 처리
     * - 액세스 토큰에서 사용자 식별값과 만료 시간을 추출
     * - 화이트리스트와 리프레시 토큰을 제거
     * - 남은 만료 시간만큼 블랙리스트에 등록
     */
    public void execute(String accessToken) {
        String userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));

        Duration expiration = tokenProvider.getRemainingDuration(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ACCESS_TOKEN));

        tokenWhitelistService.deleteWhitelistToken(accessToken);
        refreshTokenService.deleteRefreshToken(userNo);
        tokenBlacklistService.blacklist(accessToken, expiration);
    }
}
