package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.ResetPasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User")
public interface ResetPasswordApiSpec {

    @Operation(
            summary = "비밀번호 재설정 API",
            description = "이메일 인증 완료 후 새로운 비밀번호로 변경합니다."
    )
    @PostMapping("/api/users/password/reset")
    ResponseEntity<Void> reset(
            @RequestBody ResetPasswordRequest request
    );
}
