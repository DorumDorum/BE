package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomParticipantResponse;
import com.project.dorumdorum.domain.chat.application.usecase.LoadMessageRoomParticipantsUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LoadMessageRoomParticipantsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMessageRoomParticipantsController implements LoadMessageRoomParticipantsApiSpec {

    private final LoadMessageRoomParticipantsUseCase loadMessageRoomParticipantsUseCase;

    @Override
    public BaseResponse<List<LoadMessageRoomParticipantResponse>> loadParticipants(
        @CurrentUser String userNo,
        @PathVariable String messageRoomNo
    ) {
        return BaseResponse.onSuccess(
            loadMessageRoomParticipantsUseCase.execute(userNo, messageRoomNo)
        );
    }
}
