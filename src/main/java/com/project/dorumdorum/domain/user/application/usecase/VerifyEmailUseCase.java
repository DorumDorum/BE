package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final EmailVerificationService emailVerificationService;

    /**
     * 이메일 인증 코드 검증
     * - 이메일과 인증 코드의 일치 여부를 확인
     * - 유효하지 않으면 예외를 발생
     */
    public void execute(String email, String code) {
        emailVerificationService.verifyCode(email, code);
    }
}
