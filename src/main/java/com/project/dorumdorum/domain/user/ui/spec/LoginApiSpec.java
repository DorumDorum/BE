package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface LoginApiSpec {

    @Operation(
            summary = "로그인 API",
            description = "사용자가 이메일/비밀번호를 입력해 로그인하고, "
                    + "JWT 토큰을 포함한 인증 정보를 반환합니다."
    )
    @PostMapping("/api/users/login")
    ResponseEntity<LoginResponse> login(
            @RequestBody(
                    description = "로그인 요청 바디 (이메일, 비밀번호)",
                    required = true
            )
            LoginRequest request
    );
}
