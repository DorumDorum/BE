package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.response.MyUserChecklistResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "User")
public interface LoadUserChecklistApiSpec {

    @Operation(
            summary = "내 체크리스트 조회 API"
    )
    @GetMapping("/api/users/me/checklist")
    BaseResponse<MyUserChecklistResponse> loadMyChecklist(
            @Parameter(hidden = true) Long userNo
    );

    @Operation(
            summary = "특정 유저 체크리스트 조회 API"
    )
    @GetMapping("/api/users/{targetUserNo}/checklist")
    BaseResponse<MyUserChecklistResponse> loadUserChecklist(
            @Parameter(description = "조회할 유저 번호") Long targetUserNo
    );
}
