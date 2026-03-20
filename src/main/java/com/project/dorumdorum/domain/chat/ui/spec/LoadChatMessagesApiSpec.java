package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatMessageSummary;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Chat", description = "채팅 API")
public interface LoadChatMessagesApiSpec {

    @Operation(
            summary = "채팅 메시지 목록 조회",
            description = "채팅방의 메시지를 최신순으로 커서 기반 페이지네이션으로 조회합니다."
    )
    @GetMapping("/api/chat/rooms/{chatRoomNo}/messages")
    ResponseEntity<BaseResponse<CursorPage<ChatMessageSummary>>> loadChatMessages(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "채팅방 번호") @PathVariable String chatRoomNo,
            @Parameter(description = "다음 페이지 커서") @RequestParam(required = false) String cursor
    );
}
