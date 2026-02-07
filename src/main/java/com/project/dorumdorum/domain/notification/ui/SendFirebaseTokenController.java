package com.project.dorumdorum.domain.notification.ui;

import com.project.dorumdorum.domain.notification.application.dto.request.SendFirebaseTokenRequest;
import com.project.dorumdorum.domain.notification.application.usecase.SendFirebaseTokenUseCase;
import com.project.dorumdorum.domain.notification.ui.spec.SendFirebaseTokenApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendFirebaseTokenController implements SendFirebaseTokenApiSpec {

    private final SendFirebaseTokenUseCase sendFirebaseTokenUseCase;

    @Override
    public BaseResponse<Void> sendFirebaseToken(
            @CurrentUser String userNo,
            @RequestBody @Valid SendFirebaseTokenRequest request
    ) {
        sendFirebaseTokenUseCase.execute(userNo, request);
        return BaseResponse.onSuccess();
    }
}
