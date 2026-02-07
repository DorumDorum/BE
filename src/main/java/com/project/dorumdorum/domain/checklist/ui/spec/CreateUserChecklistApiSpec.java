package com.project.dorumdorum.domain.checklist.ui.spec;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Checklist")
public interface CreateUserChecklistApiSpec {

    @Operation(
            summary = "체크리스트 생성 API",
            description = "현재 로그인한 사용자의 체크리스트를 생성합니다."
    )
    @PostMapping("/api/users/me/checklist")
    BaseResponse<Void> create(
            @Parameter(hidden = true) String userNo,
            @RequestBody(
                    description = "생성할 체크리스트 정보",
                    required = true
            )
            CreateUserChecklistRequest request
    );
}
