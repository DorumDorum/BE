package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Message", description = "채팅/메시지 전송 API")
public interface LoadMessageRoomsApiSpec {

    @Operation(
            summary = "채팅방 목록 조회(커서 기반)",
            description = "참여 중인 채팅방을 최신 메시지 기준으로 커서 기반 조회합니다. "
                    + "첫 호출은 cursor 없이 요청하고, 이후 응답의 nextCursor를 cursor로 전달합니다. "
                    + "roomStatus가 REQUESTED인 경우 messageRequestNo가 포함됩니다."
    )
    @GetMapping("/api/message-rooms")
    BaseResponse<CursorPage<LoadMessageRoomResponse>> loadMessageRoomList(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "다음 페이지 커서", required = false)
            @RequestParam(required = false) String cursor
    );
}
