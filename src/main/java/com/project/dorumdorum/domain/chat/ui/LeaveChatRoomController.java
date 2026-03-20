package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.usecase.LeaveChatRoomUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LeaveChatRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeaveChatRoomController implements LeaveChatRoomApiSpec {

    private final LeaveChatRoomUseCase leaveChatRoomUseCase;

    @Override
    public ResponseEntity<BaseResponse<Void>> leave(
            @CurrentUser String userNo,
            @PathVariable String chatRoomNo
    ) {
        leaveChatRoomUseCase.execute(chatRoomNo, userNo);
        return ResponseEntity.ok(BaseResponse.onSuccess());
    }
}
