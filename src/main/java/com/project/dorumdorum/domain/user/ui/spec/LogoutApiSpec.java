package com.project.dorumdorum.domain.user.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "User")
public interface LogoutApiSpec {

    @Operation(
            summary = "로그아웃 API",
            description = "현재 로그인된 사용자의 액세스 토큰을 무효화하여 로그아웃 처리합니다."
    )
    @DeleteMapping("/api/users/logout")
    ResponseEntity<Void> logout(
            HttpServletResponse response,
            @Parameter(hidden = true) String accessToken
    );
}
