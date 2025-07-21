package com.project.dorumdorum.domain.user.domain.service;

import com.project.dorumdorum.domain.user.domain.repository.VerificationCodeRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.properties.IncludeEmailDomainProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.FAILED_EMAIL_VERIFICATION;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final IncludeEmailDomainProperties includeEmailDomainProperties;
    private final VerificationCodeRepository verificationCodeRepository;

    public boolean isAllowedUniversityEmail(String email) {
        String domain = email.substring(email.indexOf('@') + 1);
        return includeEmailDomainProperties.matches(domain);
    }

    public void sendCode(String email, String code) {
        verificationCodeRepository.save(email, code);
        // todo: SMTP
    }

    public void verifyCode(String email, String code) {
        verificationCodeRepository.findByEmail(email)
                .filter(code::equals)
                .orElseThrow(() -> new RestApiException(FAILED_EMAIL_VERIFICATION));

        verificationCodeRepository.delete(email);
    }
}
