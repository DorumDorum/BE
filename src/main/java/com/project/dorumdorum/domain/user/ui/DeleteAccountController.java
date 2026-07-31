package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.DeleteAccountRequest;
import com.project.dorumdorum.domain.user.application.usecase.DeleteAccountUseCase;
import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.ui.spec.DeleteAccountApiSpec;
import com.project.dorumdorum.global.annotation.AccessToken;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.security.cookie.AuthCookieWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteAccountController implements DeleteAccountApiSpec {

    private final DeleteAccountUseCase deleteAccountUseCase;
    private final LogoutUseCase logoutUseCase;
    private final AuthCookieWriter authCookieWriter;

    @Override
    public ResponseEntity<Void> delete(
            HttpServletResponse response,
            @CurrentUser String userNo,
            @AccessToken String accessToken,
            @Valid @RequestBody(required = false) DeleteAccountRequest request
    ) {
        deleteAccountUseCase.execute(userNo, request == null ? new DeleteAccountRequest(null) : request);
        logoutUseCase.execute(accessToken);
        authCookieWriter.expireAuthCookies(response);
        return ResponseEntity.noContent().build();
    }
}
