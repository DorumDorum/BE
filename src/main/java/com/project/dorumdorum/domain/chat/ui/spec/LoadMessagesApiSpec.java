package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessagesResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Message", description = "채팅/메시지 API")
public interface LoadMessagesApiSpec {

    @Operation(
            summary = "채팅방 메시지 목록 조회(커서 기반)",
            description = "특정 채팅방의 메시지를 커서 기반으로 조회합니다. "
                    + "첫 호출은 cursor 없이 요청하고, 이후 응답의 nextCursor를 cursor로 전달합니다. "
                    + "메시지는 최신순(내림차순)으로 정렬됩니다."
    )
    @GetMapping("/api/message-rooms/{messageRoomNo}/messages")
    BaseResponse<LoadMessagesResponse> loadMessages(
            @Parameter(hidden = true) String userId,
            @Parameter(description = "채팅방 ID", required = true)
            @PathVariable String messageRoomNo,
            @Parameter(description = "다음 페이지 커서 (메시지 ID)", required = false)
            @RequestParam(required = false) String cursor,
            @Parameter(description = "페이지 크기 (기본값: 20)", required = false)
            @RequestParam(required = false) Integer size
    );
}
