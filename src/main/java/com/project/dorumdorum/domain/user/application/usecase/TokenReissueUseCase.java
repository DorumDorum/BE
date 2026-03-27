package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.service.TokenReissueService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class TokenReissueUseCase {

    private final TokenReissueService tokenReissueService;
    private final TokenProvider tokenProvider;

    /**
     * 토큰 재발급
     * - 리프레시 토큰에서 사용자 식별값을 추출
     * - 유효한 토큰이면 새 토큰 묶음을 재발급
     */
    public TokenReissueResponse execute(String refreshToken) {
        String userNo = tokenProvider.getId(refreshToken)
                .orElseThrow(() -> new RestApiException(INVALID_REFRESH_TOKEN));

        return tokenReissueService.reissue(refreshToken, userNo);
    }
}
