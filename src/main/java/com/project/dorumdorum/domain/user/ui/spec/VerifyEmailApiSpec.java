package com.project.dorumdorum.domain.user.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface VerifyEmailApiSpec {

    @Operation(
            summary = "이메일 인증 API",
            description = "사용자가 이메일로 받은 인증 코드를 서버에 제출하여 이메일 소유 여부를 확인합니다."
    )
    @PostMapping("/api/email/verify")
    ResponseEntity<Void> verifyEmail(
            @Parameter(description = "인증할 사용자 이메일") String email,
            @Parameter(description = "이메일로 전송된 인증 코드") String code
    );
}
