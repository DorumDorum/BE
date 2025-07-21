package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.util.SecureRandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.INVALID_EMAIL_DOMAIN;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._EXIST_ENTITY;

@Service
@RequiredArgsConstructor
public class EmailVerificationUseCase {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final SecureRandomGenerator secureRandomGenerator;

    public void send(String email) {
        if (userService.isAlreadyRegistered(email))
            throw new RestApiException(_EXIST_ENTITY);

        if(emailVerificationService.isAllowedUniversityEmail(email))
            throw new RestApiException(INVALID_EMAIL_DOMAIN);

        String code = secureRandomGenerator.generate();
        emailVerificationService.sendCode(email, code);
    }


    public void verify(String email, String code) {
        emailVerificationService.verifyCode(email, code);
    }
}
