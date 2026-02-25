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

    public TokenReissueResponse execute(String refreshToken) {
        String userNo = tokenProvider.getId(refreshToken)
                .orElseThrow(() -> new RestApiException(INVALID_REFRESH_TOKEN));

        return tokenReissueService.reissue(refreshToken, userNo);
    }
}
