package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.DecideApplicationRequestUseCase;
import com.project.dorumdorum.domain.room.ui.spec.DecideApplicationRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideApplicationRequestController implements DecideApplicationRequestApiSpec {

    private final DecideApplicationRequestUseCase decideApplicationRequestUseCase;

    @Override
    public BaseResponse<Void> approve(
            @CurrentUser String userNo,
            @PathVariable String roomNo,
            @PathVariable String requestNo
    ) {
        decideApplicationRequestUseCase.approve(userNo, roomNo, requestNo);
        return BaseResponse.onSuccess();
    }

    @Override
    public BaseResponse<Void> reject(
            @CurrentUser String userNo,
            @PathVariable String requestNo
    ) {
        decideApplicationRequestUseCase.reject(userNo, requestNo);
        return BaseResponse.onSuccess();
    }
}
