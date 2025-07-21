package com.project.dorumdorum.domain.user.domain.service;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;
import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_REFRESH_TOKEN;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenReissueService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public TokenReissueResponse reissue(String refreshToken, Long userNo) {
        if (!refreshTokenService.isExist(refreshToken, userNo))
            throw new RestApiException(INVALID_REFRESH_TOKEN);

        refreshTokenService.deleteRefreshToken(userNo);

        User user = userService.findUser(userNo);
        String newAccessToken = tokenProvider.createAccessToken(userNo, user.getRole());
        String newRefreshToken = tokenProvider.createRefreshToken(userNo, user.getRole());
        Duration duration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_MEMBER_JWT));

        refreshTokenService.saveRefreshToken(userNo, newRefreshToken, duration);

        return new TokenReissueResponse(newAccessToken, newRefreshToken);
    }
}
