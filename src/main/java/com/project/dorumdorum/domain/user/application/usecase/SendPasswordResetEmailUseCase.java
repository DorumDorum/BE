package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.ratelimit.RateLimited;
import com.project.dorumdorum.global.util.SecureRandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_EMAIL_DOMAIN;

@Service
@RequiredArgsConstructor
public class SendPasswordResetEmailUseCase {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final SecureRandomGenerator secureRandomGenerator;

    /**
     * 비밀번호 재설정 인증 코드 발송
     * - 허용된 대학 이메일 도메인인지 검증
     * - 가입된 이메일인 경우에만 실제로 코드를 발송 (미가입 이메일도 동일한 200 응답 반환)
     */
    @RateLimited(tag = "password-reset-email", key = "#email")
    public void send(String email) {
        if (!emailVerificationService.isAllowedUniversityEmail(email)) {
            throw new RestApiException(INVALID_EMAIL_DOMAIN);
        }

        if (!userService.isAlreadyRegistered(email)) {
            return;
        }

        String code = secureRandomGenerator.generate();
        emailVerificationService.sendCode(email, code);
    }
}
