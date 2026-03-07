package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.spec.TokenReissueApiSpec;
import com.project.dorumdorum.global.annotation.RefreshToken;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenReissueController implements TokenReissueApiSpec {

    private final TokenReissueUseCase tokenReissueUseCase;
    private final AuthCookieWriter authCookieWriter;

    @Override
    public ResponseEntity<Void> reissue(
            HttpServletResponse response,
            @RefreshToken String refreshToken
    ) {
        TokenReissueResponse tokens = tokenReissueUseCase.execute(refreshToken);
        authCookieWriter.writeAccessCookie(response, tokens.accessToken());
        authCookieWriter.writeRefreshCookie(response, tokens.refreshToken());

        return ResponseEntity.ok().build();
    }
}
