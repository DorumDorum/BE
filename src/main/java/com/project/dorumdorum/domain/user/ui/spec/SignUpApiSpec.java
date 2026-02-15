package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "User")
public interface SignUpApiSpec {

    @Operation(
            summary = "회원가입 API",
            description = "사용자가 이메일, 비밀번호, 닉네임 등의 정보를 입력하여 회원가입을 진행합니다."
    )
    @PostMapping("/api/users/sign-up")
    ResponseEntity<Void> signUp(
            @RequestBody(
                    description = "회원가입 요청 바디 (이름, 닉네임, 이메일, 비밀번호, 성별)",
                    required = true
            )
            SignUpRequest request
    );
}
