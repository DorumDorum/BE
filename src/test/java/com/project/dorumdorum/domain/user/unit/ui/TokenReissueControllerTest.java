package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.application.usecase.TokenReissueUseCase;
import com.project.dorumdorum.domain.user.ui.TokenReissueController;
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

    @InjectMocks
    private TokenReissueController controller;

    @Test
    @DisplayName("Should return token reissue result from service")
    void reissue_ReturnsServiceResult() {
        String userNo = "0000000000000001";
        String refreshToken = "refresh";
        TokenReissueResponse expected = new TokenReissueResponse("new-access", "new-refresh");
        when(tokenReissueUseCase.execute(userNo, refreshToken)).thenReturn(expected);

        ResponseEntity<TokenReissueResponse> response = controller.reissue(userNo, refreshToken);

        verify(tokenReissueUseCase).execute(userNo, refreshToken);
        assertThat(response.getBody()).isEqualTo(expected);
    }
}
