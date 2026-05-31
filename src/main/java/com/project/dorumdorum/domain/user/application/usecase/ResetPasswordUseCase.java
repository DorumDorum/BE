package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.ResetPasswordRequest;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.PasswordResetVerifiedRepository;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.FAILED_EMAIL_VERIFICATION;
import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus._PASSWORD_NOT_MATCHES;

@Service
@RequiredArgsConstructor
public class ResetPasswordUseCase {

    private final UserService userService;
    private final PasswordResetVerifiedRepository passwordResetVerifiedRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 비밀번호 재설정
     * - 비밀번호 일치 여부 확인
     * - 이메일 인증 완료 여부 확인
     * - 비밀번호 업데이트
     */
    @Transactional
    public void execute(ResetPasswordRequest request) {
        if (!request.isPasswordMatch()) {
            throw new RestApiException(_PASSWORD_NOT_MATCHES);
        }

        if (!passwordResetVerifiedRepository.existsByEmail(request.email())) {
            throw new RestApiException(FAILED_EMAIL_VERIFICATION);
        }

        User user = userService.findByEmail(request.email());
        user.updatePassword(passwordEncoder.encode(request.newPassword()));
        passwordResetVerifiedRepository.delete(request.email());
    }
}
