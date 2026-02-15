package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.service.TokenReissueService;
import com.project.dorumdorum.global.logging.Logging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenReissueUseCase {

    private final TokenReissueService tokenReissueService;

    @Logging(
            event = "user_auth",
            action = "TOKEN_REISSUE_COMPLETED",
            payload = {
                    "userNo=#p0"
            }
    )
    public TokenReissueResponse execute(String userNo, String refreshToken) {
        return tokenReissueService.reissue(refreshToken, userNo);
    }
}
