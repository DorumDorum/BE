package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.repository.EmailVerifiedRepository;
import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final EmailVerificationService emailVerificationService;
    private final EmailVerifiedRepository emailVerifiedRepository;

    /**
     * 이메일 인증 코드 검증
     * - 이메일과 인증 코드의 일치 여부를 확인
     * - 검증 성공 시 회원가입 가능 상태를 Redis에 저장
     */
    public void execute(String email, String code) {
        emailVerificationService.verifyCode(email, code);
        emailVerifiedRepository.save(email);
    }
}
