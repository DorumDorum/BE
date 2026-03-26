package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.usecase.GetOrCreateDirectChatRoomUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.GetOrCreateDirectChatRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetOrCreateDirectChatRoomController implements GetOrCreateDirectChatRoomApiSpec {

    private final GetOrCreateDirectChatRoomUseCase getOrCreateDirectChatRoomUseCase;

    @Override
    public ResponseEntity<String> getOrCreate(
            @CurrentUser String callerUserNo,
            @PathVariable String roomNo,
            @PathVariable String applicantUserNo
    ) {
        String chatRoomNo = getOrCreateDirectChatRoomUseCase.execute(callerUserNo, roomNo, applicantUserNo);
        return ResponseEntity.ok(chatRoomNo);
    }
}
