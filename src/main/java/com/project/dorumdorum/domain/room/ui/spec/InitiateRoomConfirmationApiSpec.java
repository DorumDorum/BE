package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface InitiateRoomConfirmationApiSpec {

    @Operation(
            summary = "방 확정 절차 시작 API",
            description = "방이 확정 단계로 진입하도록 워크플로우를 시작합니다."
    )
    @PostMapping("/api/room/{roomNo}/confirm")
    BaseResponse<Void> init(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "방 번호") Long roomNo
    );
}
