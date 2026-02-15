package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.spec.TokenReissueApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.annotation.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenReissueController implements TokenReissueApiSpec {

    private final TokenReissueUseCase tokenReissueUseCase;

    @Override
    public ResponseEntity<TokenReissueResponse> reissue(
            @CurrentUser String userNo,
            @RefreshToken String refreshToken
    ) {
        return ResponseEntity.ok(tokenReissueUseCase.execute(userNo, refreshToken));
    }
}
