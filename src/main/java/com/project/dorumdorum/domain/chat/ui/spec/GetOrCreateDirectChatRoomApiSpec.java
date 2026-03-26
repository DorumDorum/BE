package com.project.dorumdorum.domain.chat.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Chat", description = "채팅 API")
public interface GetOrCreateDirectChatRoomApiSpec {

    @Operation(
            summary = "지원자-방장 1:1 채팅방 조회 또는 생성",
            description = "지원자와 방장 간의 1:1 채팅방을 조회합니다. 존재하지 않으면 새로 생성합니다. " +
                    "호출자는 해당 방의 방장이거나 지원자(applicantUserNo)여야 합니다."
    )
    @PostMapping("/api/rooms/{roomNo}/direct-chat/{applicantUserNo}")
    ResponseEntity<String> getOrCreate(
            @Parameter(hidden = true) String callerUserNo,
            @PathVariable String roomNo,
            @PathVariable String applicantUserNo
    );
}
