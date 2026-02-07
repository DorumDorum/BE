package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "User")
public interface LoadUserProfileApiSpec {

    @Operation(
            summary = "내 프로필 조회 API",
            description = "현재 로그인한 사용자의 프로필을 조회합니다."
    )
    @GetMapping("/api/users/profile/me")
    BaseResponse<ProfileResponse> loadMyProfile(
            @Parameter(hidden = true) String userNo
    );

    @Operation(
            summary = "특정 유저 프로필 조회 API",
            description = "userNo에 해당하는 사용자의 공개 프로필을 조회합니다."
    )
    @GetMapping("/api/users/profile/{userNo}")
    BaseResponse<ProfileResponse> loadProfile(
            @Parameter(description = "번호") String userNo
    );
}
