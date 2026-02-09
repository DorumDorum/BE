package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Message", description = "채팅/메시지 전송 API")
public interface AddParticipantToMessageRoomApiSpec {

    @Operation(
            summary = "채팅방에 유저 추가",
            description = "특정 유저를 채팅방에 참여자로 추가합니다. GROUP 채팅방에서만 사용 가능합니다."
    )
    @PostMapping("/api/chat/rooms/{messageRoomNo}/participants/{targetUserNo}")
    BaseResponse<Void> addParticipant(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "채팅방 번호") @PathVariable String messageRoomNo,
            @Parameter(description = "추가할 유저 번호") @PathVariable String targetUserNo
    );
}
