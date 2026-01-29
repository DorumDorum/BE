package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyRoomRuleUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadMyRoomRuleApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyRoomRuleController implements LoadMyRoomRuleApiSpec {

    private final LoadMyRoomRuleUseCase loadMyRoomRuleUseCase;

    @Override
    public BaseResponse<MyRoomRuleResponse> load(
            @CurrentUser Long userNo,
            @RequestParam Long roomNo
    ) {
        return BaseResponse.onSuccess(loadMyRoomRuleUseCase.execute(userNo, roomNo));
    }
}
