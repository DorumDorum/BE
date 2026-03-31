package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.util.SecureRandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_EMAIL_DOMAIN;
import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.DUPLICATE_EMAIL;

@Service
@RequiredArgsConstructor
public class SendVerificationEmailUseCase {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final SecureRandomGenerator secureRandomGenerator;

    /**
     * 이메일 인증 코드 발송
     * - 이미 가입된 이메일인지 확인
     * - 허용된 대학 이메일 도메인인지 검증
     * - 인증 코드를 생성해 메일로 발송
     */
    public void send(String email) {
        if (userService.isAlreadyRegistered(email)) {
            throw new RestApiException(DUPLICATE_EMAIL);
        }

        if(!emailVerificationService.isAllowedUniversityEmail(email)) {
            throw new RestApiException(INVALID_EMAIL_DOMAIN);
        }

        String code = secureRandomGenerator.generate();
        emailVerificationService.sendCode(email, code);
    }
}
