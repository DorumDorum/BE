package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.LogoutUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LogoutApiSpec;
import com.project.dorumdorum.global.annotation.AccessToken;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogoutController implements LogoutApiSpec {

    private final LogoutUseCase logoutUseCase;

    @Override
    public BaseResponse<Void> logout(
            @AccessToken String accessToken
    ) {
        logoutUseCase.execute(accessToken);
        return BaseResponse.onSuccess();
    }
}
