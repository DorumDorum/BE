package com.project.dorumdorum.domain.user.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface TokenReissueApiSpec {

    @Operation(
            summary = "토큰 재발급 API",
            description = "만료된 액세스 토큰을 갱신하기 위해 리프레시 토큰을 이용하여 새로운 액세스 토큰을 발급하고, "
                    + "액세스 토큰과 리프레시 토큰을 모두 HttpOnly 쿠키로 재설정합니다."
    )
    @PostMapping("/api/token/reissue")
    ResponseEntity<Void> reissue(
            HttpServletResponse response,
            String refreshToken
    );
}
