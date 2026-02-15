package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoginApiSpec;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApiSpec {

    private final LoginUseCase loginUseCase;

    @Override
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return ResponseEntity.ok(loginUseCase.execute(request));
    }
}
