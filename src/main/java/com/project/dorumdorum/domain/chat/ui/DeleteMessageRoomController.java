package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.usecase.DeleteMessageRoomUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.DeleteMessageRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteMessageRoomController implements DeleteMessageRoomApiSpec {

    private final DeleteMessageRoomUseCase deleteMessageRoomUseCase;

    @Override
    public BaseResponse<Void> deleteMessageRoom(
            @CurrentUser String userNo,
            @PathVariable String messageRoomNo
    ) {
        deleteMessageRoomUseCase.execute(userNo, messageRoomNo);
        return BaseResponse.onSuccess();
    }
}
