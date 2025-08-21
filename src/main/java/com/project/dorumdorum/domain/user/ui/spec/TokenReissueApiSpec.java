package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.response.TokenReissueResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface TokenReissueApiSpec {

    @Operation(
            summary = "토큰 재발급 API",
            description = "만료된 액세스 토큰을 갱신하기 위해 리프레시 토큰을 이용하여 새로운 액세스 토큰과 리프레시 토큰을 발급합니다."
    )
    @PostMapping("/api/token/reissue")
    BaseResponse<TokenReissueResponse> reissue(
            @Parameter(hidden = true) Long userNo,
            @Parameter(hidden = true) String refreshToken
    );
}
