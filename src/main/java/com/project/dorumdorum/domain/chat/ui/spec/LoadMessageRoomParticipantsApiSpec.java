package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.domain.chat.application.dto.response.LoadMessageRoomParticipantResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Message", description = "채팅/메시지 API")
public interface LoadMessageRoomParticipantsApiSpec {

    @Operation(
        summary = "채팅방 참여자 목록 조회",
        description = "특정 채팅방에 참여 중인 유저 목록을 조회합니다. "
            + "상위 room이 확정(COMPLETED) 상태라면 실명/학번/전공을 노출합니다."
    )
    @GetMapping("/api/chat/rooms/{messageRoomNo}/participants")
    BaseResponse<List<LoadMessageRoomParticipantResponse>> loadParticipants(
        @Parameter(hidden = true) String userNo,
        @Parameter(description = "채팅방 번호") @PathVariable String messageRoomNo
    );
}
