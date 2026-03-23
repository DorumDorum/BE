package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.usecase.MarkChatRoomReadUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.MarkChatRoomReadApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MarkChatRoomReadController implements MarkChatRoomReadApiSpec {

    private final MarkChatRoomReadUseCase markChatRoomReadUseCase;

    @Override
    public ResponseEntity<Void> markAsRead(
            @CurrentUser String userNo,
            @PathVariable String chatRoomNo
    ) {
        markChatRoomReadUseCase.execute(chatRoomNo, userNo);
        return ResponseEntity.noContent().build();
    }
}
