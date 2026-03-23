package com.project.dorumdorum.domain.chat.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Chat", description = "채팅 API")
public interface LeaveChatRoomApiSpec {

    @Operation(
            summary = "채팅방 퇴장",
            description = "채팅방에서 퇴장합니다. 다른 멤버가 있는 경우 방장은 퇴장할 수 없습니다."
    )
    @DeleteMapping("/api/chat/rooms/{chatRoomNo}/leave")
    ResponseEntity<Void> leave(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "채팅방 번호") @PathVariable String chatRoomNo
    );
}
