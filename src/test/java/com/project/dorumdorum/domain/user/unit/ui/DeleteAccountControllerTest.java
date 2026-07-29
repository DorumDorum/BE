package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.request.DeleteAccountRequest;
import com.project.dorumdorum.domain.user.application.usecase.DeleteAccountUseCase;
import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.ui.DeleteAccountController;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAccountController 단위 테스트")
class DeleteAccountControllerTest {

    @Mock
    private DeleteAccountUseCase deleteAccountUseCase;

    @Mock
    private LogoutUseCase logoutUseCase;

    @Mock
    private AuthCookieWriter authCookieWriter;

    @Mock
    private HttpServletResponse servletResponse;

    @InjectMocks
    private DeleteAccountController controller;

    @Test
    @DisplayName("delete는 탈퇴 처리 후 토큰과 쿠키를 정리하고 204를 반환한다")
    void delete_DeletesAccountAndExpiresAuth() {
        DeleteAccountRequest request = new DeleteAccountRequest("서비스를 자주 쓰지 않아요");

        ResponseEntity<Void> response = controller.delete(servletResponse, "user-1", "access-token", request);

        verify(deleteAccountUseCase).execute("user-1", request);
        verify(logoutUseCase).execute("access-token");
        verify(authCookieWriter).expireAuthCookies(servletResponse);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
