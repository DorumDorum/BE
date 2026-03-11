package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoginApiSpec;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApiSpec {

    private final LoginUseCase loginUseCase;
    private final AuthCookieWriter authCookieWriter;

    @Override
    public ResponseEntity<Void> login(
            HttpServletResponse response,
            @RequestBody @Valid LoginRequest request
    ) {
        LoginResponse tokens = loginUseCase.execute(request);
        authCookieWriter.writeAccessCookie(response, tokens.accessToken());
        authCookieWriter.writeRefreshCookie(response, tokens.refreshToken());

        return ResponseEntity.ok().build();
    }
}
