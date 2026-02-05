package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.room.application.usecase.UpdateRoomRuleUseCase;
import com.project.dorumdorum.domain.room.ui.spec.UpdateRoomRuleApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateRoomRuleController implements UpdateRoomRuleApiSpec {

    private final UpdateRoomRuleUseCase updateRoomRuleUseCase;

    @Override
    public BaseResponse<Void> update(
            @CurrentUser String userNo,
            @RequestParam String roomNo,
            @RequestBody UpdateRoomRuleRequest request
    ) {
        updateRoomRuleUseCase.execute(userNo, roomNo, request);
        return BaseResponse.onSuccess(null);
    }
}
