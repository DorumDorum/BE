package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.usecase.AddParticipantToMessageRoomUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.AddParticipantToMessageRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AddParticipantToMessageRoomController implements AddParticipantToMessageRoomApiSpec {

    private final AddParticipantToMessageRoomUseCase addParticipantToMessageRoomUseCase;

    @Override
    public BaseResponse<Void> addParticipant(
            @CurrentUser Long userNo,
            @PathVariable Long messageRoomNo,
            @PathVariable Long targetUserNo
    ) {
        addParticipantToMessageRoomUseCase.execute(userNo, messageRoomNo, targetUserNo);
        return BaseResponse.onSuccess();
    }
}
