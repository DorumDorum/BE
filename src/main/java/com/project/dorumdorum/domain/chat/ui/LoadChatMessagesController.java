package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageSummary;
import com.project.dorumdorum.domain.chat.application.usecase.LoadChatMessagesUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.LoadChatMessagesApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.pagination.CursorPage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadChatMessagesController implements LoadChatMessagesApiSpec {

    private final LoadChatMessagesUseCase loadChatMessagesUseCase;

    @Override
    public ResponseEntity<CursorPage<ChatMessageSummary>> loadChatMessages(
            @CurrentUser String userNo,
            @PathVariable String chatRoomNo,
            @RequestParam(required = false) String cursor
    ) {
        return ResponseEntity.ok(loadChatMessagesUseCase.execute(chatRoomNo, userNo, cursor));
    }
}
