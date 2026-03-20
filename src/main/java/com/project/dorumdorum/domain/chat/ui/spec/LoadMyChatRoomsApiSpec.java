package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomSummary;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Chat", description = "채팅 API")
public interface LoadMyChatRoomsApiSpec {

    @Operation(
            summary = "내 채팅방 목록 조회",
            description = "현재 로그인한 사용자가 참여 중인 채팅방 목록을 최신 메시지 기준으로 조회합니다."
    )
    @GetMapping("/api/chat/rooms")
    ResponseEntity<BaseResponse<List<ChatRoomSummary>>> loadMyChatRooms(
            @Parameter(hidden = true) String userNo
    );
}
