package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.request.DecideMessageRequest;
import com.project.dorumdorum.domain.chat.application.usecase.DecideMessageRequestUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.DecideMessageRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DecideMessageRequestController implements DecideMessageRequestApiSpec {

    private final DecideMessageRequestUseCase decideMessageRequestUseCase;

    @Override
    public BaseResponse<Void> decideMessageRequest(
        @CurrentUser Long userNo,
        @PathVariable Long messageRequestNo,
        @RequestBody DecideMessageRequest request
    ) {
        decideMessageRequestUseCase.execute(userNo, messageRequestNo, request);
        return BaseResponse.onSuccess();
    }
}
