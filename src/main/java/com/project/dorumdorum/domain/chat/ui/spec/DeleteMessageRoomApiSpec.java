package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Message", description = "채팅/메시지 전송 API")
public interface DeleteMessageRoomApiSpec {

    @Operation(
            summary = "채팅방 삭제",
            description = "채팅방을 삭제합니다. 방장이거나 모든 참여자가 퇴장한 경우 방 상태를 DELETED로 변경합니다."
    )
    @DeleteMapping("/api/chat/rooms/{messageRoomNo}")
    BaseResponse<Void> deleteMessageRoom(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "채팅방 번호") @PathVariable Long messageRoomNo
    );
}
