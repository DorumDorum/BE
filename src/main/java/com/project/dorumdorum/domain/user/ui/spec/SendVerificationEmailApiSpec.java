package com.project.dorumdorum.domain.user.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface SendVerificationEmailApiSpec {

    @Operation(
            summary = "인증 이메일 발송 API",
            description = "사용자가 입력한 이메일 주소로 인증 메일을 전송합니다. "
                    + "회원가입 시 사용됩니다."
    )
    @PostMapping("/api/email/send")
    ResponseEntity<Void> send(
            @Parameter(description = "인증 이메일을 받을 사용자 이메일 주소") String email
    );
}
