package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.domain.user.domain.service.TokenReissueService;
import com.project.dorumdorum.domain.user.ui.TokenReissueController;
import com.project.dorumdorum.global.common.BaseResponse;
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
    private TokenReissueService tokenReissueService;

    @InjectMocks
    private TokenReissueController controller;

    @Test
    @DisplayName("Should return token reissue result from service")
    void reissue_ReturnsServiceResult() {
        String userNo = "0000000000000001";
        String refreshToken = "refresh";
        TokenReissueResponse expected = new TokenReissueResponse("new-access", "new-refresh");
        when(tokenReissueService.reissue(refreshToken, userNo)).thenReturn(expected);

        BaseResponse<TokenReissueResponse> response = controller.reissue(userNo, refreshToken);

        verify(tokenReissueService).reissue(refreshToken, userNo);
        assertThat(response.getResult()).isEqualTo(expected);
    }
}
