package com.project.dorumdorum.domain.user.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface SendPasswordResetEmailApiSpec {

    @Operation(
            summary = "비밀번호 재설정 인증 이메일 발송 API",
            description = "가입된 학교 이메일 주소로 비밀번호 재설정 인증 코드를 전송합니다."
    )
    @PostMapping("/api/email/password-reset/send")
    ResponseEntity<Void> send(
            @Parameter(description = "비밀번호를 재설정할 사용자 이메일 주소") String email
    );
}
