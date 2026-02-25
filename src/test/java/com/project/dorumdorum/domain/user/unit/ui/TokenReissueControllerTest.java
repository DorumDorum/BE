package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.response.AuthTokenResponse;
import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.TokenReissueController;
import com.project.dorumdorum.global.properties.JwtProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenReissueController Unit Tests")
class TokenReissueControllerTest {

    @Mock
    private TokenReissueUseCase tokenReissueUseCase;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private TokenReissueController controller;

    @Test
    @DisplayName("Should return new access token in body and set refresh token cookie")
    void reissue_ReturnsAccessTokenAndSetsRefreshCookie() {
        String userNo = "0000000000000001";
        String refreshToken = "refresh";
        TokenReissueResponse useCaseResponse = new TokenReissueResponse("new-access", "new-refresh");
        when(tokenReissueUseCase.execute(userNo, refreshToken)).thenReturn(useCaseResponse);
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(3600L);

        ResponseEntity<AuthTokenResponse> response = controller.reissue(userNo, refreshToken);

        verify(tokenReissueUseCase).execute(userNo, refreshToken);
        assertThat(response.getBody()).isEqualTo(new AuthTokenResponse("new-access"));

        String setCookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(setCookieHeader).isNotNull();
        assertThat(setCookieHeader).contains("new-refresh");
        assertThat(setCookieHeader).contains("HttpOnly");
    }
}
