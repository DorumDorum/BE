package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Chat", description = "채팅 API")
public interface GetChatRoomMembersApiSpec {

    @Operation(
            summary = "채팅방 멤버 목록 조회",
            description = "채팅방의 현재 멤버 목록을 반환합니다. 호스트가 맨 앞에 옵니다."
    )
    @GetMapping("/api/chat/rooms/{chatRoomNo}/members")
    ResponseEntity<List<ChatRoomMemberResponse>> getMembers(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "채팅방 번호") @PathVariable String chatRoomNo
    );
}
