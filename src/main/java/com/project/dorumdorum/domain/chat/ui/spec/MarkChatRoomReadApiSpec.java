package com.project.dorumdorum.domain.chat.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Chat", description = "채팅 API")
public interface MarkChatRoomReadApiSpec {

    @Operation(
            summary = "채팅방 읽음 처리",
            description = "채팅방의 미읽은 메시지를 읽음 처리합니다."
    )
    @PostMapping("/api/chat/rooms/{chatRoomNo}/read")
    ResponseEntity<Void> markAsRead(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "채팅방 번호") @PathVariable String chatRoomNo
    );
}
