package com.project.dorumdorum.domain.chat.ui;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomMemberResponse;
import com.project.dorumdorum.domain.chat.application.usecase.GetChatRoomMembersUseCase;
import com.project.dorumdorum.domain.chat.ui.spec.GetChatRoomMembersApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetChatRoomMembersController implements GetChatRoomMembersApiSpec {

    private final GetChatRoomMembersUseCase getChatRoomMembersUseCase;

    @Override
    public ResponseEntity<List<ChatRoomMemberResponse>> getMembers(
            @CurrentUser String userNo,
            @PathVariable String chatRoomNo
    ) {
        return ResponseEntity.ok(getChatRoomMembersUseCase.execute(chatRoomNo, userNo));
    }
}
