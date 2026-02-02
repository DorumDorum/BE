package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import com.project.dorumdorum.domain.room.application.usecase.UpdateRoomTitleUseCase;
import com.project.dorumdorum.domain.room.ui.spec.UpdateRoomTitleApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateRoomTitleController implements UpdateRoomTitleApiSpec {

    private final UpdateRoomTitleUseCase updateRoomTitleUseCase;

    @Override
    public BaseResponse<Void> update(
            @CurrentUser Long userNo,
            @RequestParam Long roomNo,
            @RequestBody UpdateRoomTitleRequest request
    ) {
        updateRoomTitleUseCase.execute(userNo, roomNo, request);
        return BaseResponse.onSuccess(null);
    }
}
