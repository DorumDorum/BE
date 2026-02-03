package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Room")
public interface LoadMyRoomRuleApiSpec {

    @Operation(
            summary = "내가 속한 방 규칙 조회 API"
    )
    @GetMapping("/api/rooms/{roomNo}/rule")
    BaseResponse<MyRoomRuleResponse> load(
            @Parameter(description = "규칙 조회하려는 방 번호") Long roomNo
    );
}
