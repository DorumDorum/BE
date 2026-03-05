package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface LoginApiSpec {

    @Operation(
            summary = "로그인 API",
            description = "사용자가 이메일/비밀번호를 입력해 로그인하고, "
                    + "액세스 토큰과 리프레시 토큰을 모두 HttpOnly 쿠키로 반환합니다."
    )
    @PostMapping("/api/users/login")
    ResponseEntity<Void> login(
            HttpServletResponse response,
            @RequestBody(
                    description = "로그인 요청 바디 (이메일, 비밀번호)",
                    required = true
            )
            LoginRequest request
    );
}
