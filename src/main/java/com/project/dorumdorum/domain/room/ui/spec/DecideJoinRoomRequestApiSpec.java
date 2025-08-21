package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface DecideJoinRoomRequestApiSpec {

    @Operation(
            summary = "방 참여 요청 승인 API",
            description = "방 참여 요청(requestNo)을 승인합니다. 승인 시 대상 사용자는 방에 참여 처리됩니다."
    )
    @PostMapping("/api/rooms/{roomNo}/join-request/{requestNo}/approve")
    BaseResponse<Void> approve(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "방 번호") Long roomNo,
            @Parameter(description = "입장 요청 번호") Long requestNo
    );

    @Operation(
            summary = "방 참여 요청 거절 API",
            description = "방 참여 요청(requestNo)을 거절합니다. 거절 시 요청은 무효 처리됩니다."
    )
    @PostMapping("/api/join-request/{requestNo}/reject")
    BaseResponse<Void> reject(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "입장 요청 번호") Long requestNo
    );
}
