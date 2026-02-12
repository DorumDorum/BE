package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadMyRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyRoomController implements LoadMyRoomsApiSpec {

    private final LoadMyRoomsUseCase loadMyRoomsUseCase;

    @Override
    public BaseResponse<FindRoomsResponse> load(
            @CurrentUser String userNo
    ) {
        return BaseResponse.onSuccess(loadMyRoomsUseCase.execute(userNo));
    }
}
