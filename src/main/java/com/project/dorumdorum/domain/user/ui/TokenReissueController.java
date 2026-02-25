package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.AuthTokenResponse;
import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.spec.TokenReissueApiSpec;
import com.project.dorumdorum.global.annotation.RefreshToken;
import com.project.dorumdorum.global.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class TokenReissueController implements TokenReissueApiSpec {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final TokenReissueUseCase tokenReissueUseCase;
    private final JwtProperties jwtProperties;

    @Override
    public ResponseEntity<AuthTokenResponse> reissue(
            @RefreshToken String refreshToken
    ) {
        TokenReissueResponse tokens = tokenReissueUseCase.execute(refreshToken);

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, tokens.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofSeconds(jwtProperties.getRefreshTokenExpiration()))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new AuthTokenResponse(tokens.accessToken()));
    }
}
