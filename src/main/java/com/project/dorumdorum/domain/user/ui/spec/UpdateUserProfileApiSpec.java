package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "User")
public interface UpdateUserProfileApiSpec {

    @Operation(
            summary = "내 프로필 수정 API",
            description = "현재 로그인한 사용자의 프로필 정보를 수정합니다. "
                    + "이름, 닉네임, 이메일 등의 정보를 변경할 수 있습니다."
    )
    @PatchMapping("/api/users/profile")
    ResponseEntity<ProfileResponse> updateProfile(
            @Parameter(hidden = true) String userNo,
            @RequestBody(
                    description = "수정할 프로필 정보 (이름, 닉네임, 이메일 등)",
                    required = true
            )
            UpdateProfileRequest request
    );
}
