package com.project.dorumdorum.domain.user.ui.spec;

import com.project.dorumdorum.domain.user.application.dto.request.DeleteAccountRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User")
public interface DeleteAccountApiSpec {

    @Operation(summary = "회원 탈퇴 API", description = "로그인한 사용자를 soft delete하고 인증 쿠키와 토큰을 폐기합니다.")
    @DeleteMapping("/api/users/me")
    ResponseEntity<Void> delete(
            HttpServletResponse response,
            @Parameter(hidden = true) String userNo,
            @Parameter(hidden = true) String accessToken,
            @Valid @RequestBody(required = false) DeleteAccountRequest request
    );
}
