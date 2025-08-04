package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.dto.usecase.CreateRoomUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateRoomController {

    private final CreateRoomUseCase createRoomUseCase;

    @PostMapping("/api/room")
    public BaseResponse<Void> create(
            @CurrentUser Long userNo,
            @RequestBody @Valid RoomCreateRequest request
    ) {
        createRoomUseCase.execute(userNo, request);
        return BaseResponse.onSuccess();
    }
}
