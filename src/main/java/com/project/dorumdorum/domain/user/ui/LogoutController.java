package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LogoutApiSpec;
import com.project.dorumdorum.global.annotation.AccessToken;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController implements LogoutApiSpec {

    private final LogoutUseCase logoutUseCase;
    private final AuthCookieWriter authCookieWriter;

    @Override
    public ResponseEntity<Void> logout(
            HttpServletResponse response,
            @AccessToken String accessToken
    ) {
        logoutUseCase.execute(accessToken);
        authCookieWriter.expireAuthCookies(response);
        return ResponseEntity.ok().build();
    }
}
