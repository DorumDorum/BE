package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.usecase.LeaveChatRoomUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LeaveChatRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LeaveChatRoomController implements LeaveChatRoomApiSpec {

    private final LeaveChatRoomUseCase leaveChatRoomUseCase;

    @Override
    public ResponseEntity<Void> leave(
            @CurrentUser String userNo,
            @PathVariable String chatRoomNo
    ) {
        leaveChatRoomUseCase.execute(chatRoomNo, userNo);
        return ResponseEntity.noContent().build();
    }
}
