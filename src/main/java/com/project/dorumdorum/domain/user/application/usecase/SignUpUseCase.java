package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.EmailVerifiedRepository;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.DUPLICATE_SIGN_UP_INFO;
import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus.EMAIL_NOT_VERIFIED;
import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus._PASSWORD_NOT_MATCHES;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserService userService;
    private final EmailVerifiedRepository emailVerifiedRepository;

    /**
     * 회원가입 처리
     * - 이메일 인증 완료 여부를 검증
     * - 비밀번호 확인 여부를 검증
     * - 중복 이메일 가입을 차단
     * - 사용자를 저장하고 생성된 사용자 번호를 반환
     */
    public String execute(SignUpRequest request) {
        if (!emailVerifiedRepository.existsByEmail(request.email())) {
            throw new RestApiException(EMAIL_NOT_VERIFIED);
        }

        if (!request.isCheckedPassword()) {
            throw new RestApiException(_PASSWORD_NOT_MATCHES);
        }

        if (userService.isAlreadyRegistered(request.email())
                || userService.isAlreadyRegisteredStudentNo(request.studentNo())) {
            throw new RestApiException(DUPLICATE_SIGN_UP_INFO);
        }

        User savedUser = userService.save(request);
        emailVerifiedRepository.delete(request.email());
        return savedUser.getUserNo();
    }
}
