package com.project.dorumdorum.domain.checklist.ui.spec;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Checklist")
public interface UpdateRoomRuleApiSpec {

    @Operation(
            summary = "내가 속한 방 규칙 수정 API"
    )
    @PutMapping("/api/rooms/me/rule")
    BaseResponse<Void> update(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "규칙 수정하려는 방 번호") @RequestParam String roomNo,
            @Parameter(description = "수정할 규칙 정보") @RequestBody UpdateRoomRuleRequest request
    );
}
