package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.repository.PasswordResetCodeRepository;
import com.project.dorumdorum.domain.user.domain.repository.PasswordResetVerifiedRepository;
import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.FAILED_EMAIL_VERIFICATION;

@Service
@RequiredArgsConstructor
public class VerifyPasswordResetCodeUseCase {

    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final PasswordResetVerifiedRepository passwordResetVerifiedRepository;
    private final EmailVerificationService emailVerificationService;

    /**
     * 비밀번호 재설정 인증 코드 검증
     * - 인증 코드 일치 여부 확인
     * - 검증 성공 시 비밀번호 재설정 가능 상태를 Redis에 저장
     */
    public void execute(String email, String code) {
        emailVerificationService.verifyCode(email, code);
        passwordResetVerifiedRepository.save(email);
    }
}
