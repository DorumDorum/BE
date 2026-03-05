package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.TokenReissueController;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
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
    private AuthCookieWriter authCookieWriter;

    @InjectMocks
    private TokenReissueController controller;

    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("Should delegate to use case and write cookies via AuthCookieWriter")
    void reissue_DelegatesToUseCaseAndWritesCookies() {
        String refreshToken = "refresh";
        TokenReissueResponse useCaseResponse = new TokenReissueResponse("new-access", "new-refresh");
        when(tokenReissueUseCase.execute(refreshToken)).thenReturn(useCaseResponse);

        ResponseEntity<Void> result = controller.reissue(response, refreshToken);

        verify(tokenReissueUseCase).execute(refreshToken);
        verify(authCookieWriter).writeAccessCookie(response, "new-access");
        verify(authCookieWriter).writeRefreshCookie(response, "new-refresh");
        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(result.getBody()).isNull();
    }
}
