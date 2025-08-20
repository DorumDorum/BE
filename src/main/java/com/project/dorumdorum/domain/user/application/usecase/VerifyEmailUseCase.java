package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyEmailUseCase {

    private final EmailVerificationService emailVerificationService;

    public void execute(String email, String code) {
        emailVerificationService.verifyCode(email, code);
    }
}
