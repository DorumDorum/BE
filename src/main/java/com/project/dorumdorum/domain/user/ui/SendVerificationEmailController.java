package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.SendVerificationEmailUseCase;
import com.project.dorumdorum.domain.user.ui.spec.SendVerificationEmailApiSpec;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendVerificationEmailController implements SendVerificationEmailApiSpec {

    private final SendVerificationEmailUseCase sendVerificationEmailUseCase;

    @Override
    public BaseResponse<Void> send(
            @RequestParam String email
    ) {
        sendVerificationEmailUseCase.send(email);
        return BaseResponse.onSuccess();
    }
}
