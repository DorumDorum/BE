package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface DecideRoomConfirmationApiSpec {

    @Operation(
            summary = "방 확정 수락 API",
            description = "방이 확정 단계에 도달했을 때, 사용자가 해당 방 확정에 동의(수락)합니다."
    )
    @PostMapping("/api/rooms/{roomNo}/confirm/approve")
    BaseResponse<Void> approve(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "방 번호") Long roomNo
    );

    @Operation(
            summary = "방 확정 거절 API",
            description = "방이 확정 단계에 도달했을 때, 사용자가 해당 방 확정에 거절합니다."
    )
    @PostMapping("/api/rooms/{roomNo}/confirm/reject")
    BaseResponse<Void> reject(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "방 번호") Long roomNo
    );
}
