package com.project.dorumdorum.domain.user.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface VerifyPasswordResetCodeApiSpec {

    @Operation(
            summary = "비밀번호 재설정 인증 코드 검증 API",
            description = "이메일로 받은 인증 코드를 검증합니다. 성공 시 비밀번호 재설정이 가능한 상태로 전환됩니다."
    )
    @PostMapping("/api/email/password-reset/verify")
    ResponseEntity<Void> verify(
            @Parameter(description = "인증할 사용자 이메일") String email,
            @Parameter(description = "이메일로 전송된 인증 코드") String code
    );
}
