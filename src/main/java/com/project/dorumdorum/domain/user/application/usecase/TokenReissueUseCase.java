package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.service.TokenReissueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenReissueUseCase {

    private final TokenReissueService tokenReissueService;

    public TokenReissueResponse execute(String userNo, String refreshToken) {
        return tokenReissueService.reissue(refreshToken, userNo);
    }
}
