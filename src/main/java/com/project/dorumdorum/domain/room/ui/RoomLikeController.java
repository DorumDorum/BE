package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.RoomLikeUseCase;
import com.project.dorumdorum.domain.room.ui.spec.RoomLikeApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomLikeController implements RoomLikeApiSpec {

    private final RoomLikeUseCase roomLikeUseCase;

    @Override
    public BaseResponse<Void> like(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        roomLikeUseCase.like(userNo, roomNo);
        return BaseResponse.onSuccess();
    }

    @Override
    public BaseResponse<Void> unlike(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        roomLikeUseCase.unlike(userNo, roomNo);
        return BaseResponse.onSuccess();
    }
}

