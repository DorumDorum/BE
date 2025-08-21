package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoginUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoginApiSpec;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController implements LoginApiSpec {

    private final LoginUseCase loginUseCase;

    @Override
    public BaseResponse<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        return BaseResponse.onSuccess(loginUseCase.execute(request));
    }
}
