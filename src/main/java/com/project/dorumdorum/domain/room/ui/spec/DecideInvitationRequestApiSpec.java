package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface DecideInvitationRequestApiSpec {

    @Operation(
            summary = "방 초대 응답 API",
            description = "초대 받은 유저가 받은 요청에 수락합니다."
    )
    @PostMapping("/api/rooms/{roomNo}/invite-request/{requestNo}/approve")
    BaseResponse<Void> approve(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "방 번호") Long roomNo,
            @Parameter(description = "초대 요청 번호") Long requestNo
    );
}
