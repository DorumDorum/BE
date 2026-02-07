package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.SendMessageRequest;
import com.project.dorumdorum.domain.chat.application.usecase.SendMessageRequestUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.SendMessageRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendMessageRequestController implements SendMessageRequestApiSpec {

    private final SendMessageRequestUseCase sendMessageRequestUseCase;

    @Override
    public BaseResponse<Void> send(
            @CurrentUser Long userNo,
            @PathVariable Long receiverNo,
            @RequestBody @Valid SendMessageRequest request
    ) {
        sendMessageRequestUseCase.execute(userNo, receiverNo, request);
        return BaseResponse.onSuccess();
    }
}
