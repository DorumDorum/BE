package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "User")
public interface UpdateUserChecklistApiSpec {

    @Operation(
            summary = "내 체크리스트 수정 API",
            description = "현재 로그인한 사용자의 체크리스트를 수정합니다."
    )
    @PutMapping("/api/users/me/checklist")
    BaseResponse<Void> update(
            @Parameter(hidden = true) Long userNo,
            @RequestBody(
                    description = "수정할 체크리스트 정보",
                    required = true
            )
            UpdateUserChecklistRequest request
    );
}
