package com.project.dorumdorum.domain.chat.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Message", description = "채팅/메시지 전송 API")
public interface LeaveMessageRoomApiSpec {

    @Operation(
            summary = "채팅방 나가기",
            description = "특정 채팅방에서 나갑니다. leftAt이 설정되며, 이후 해당 방의 메시지를 받을 수 없습니다."
    )
    @DeleteMapping("/api/chat/rooms/{messageRoomNo}/leave")
    BaseResponse<Void> leaveMessageRoom(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "채팅방 번호") @PathVariable Long messageRoomNo
    );
}
