package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.CheckMyRoomResponse;
import com.project.dorumdorum.domain.room.application.usecase.CheckMyRoomUseCase;
import com.project.dorumdorum.domain.room.ui.spec.CheckMyRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckMyRoomController implements CheckMyRoomApiSpec {

    private final CheckMyRoomUseCase checkMyRoomUseCase;

    @Override
    public BaseResponse<CheckMyRoomResponse> check(
            @CurrentUser Long userNo
    ) {
        return BaseResponse.onSuccess(checkMyRoomUseCase.execute(userNo));
    }
}
