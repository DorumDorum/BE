package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.ALREADY_REGISTERED_EMAIL;
import static com.project.dorumdorum.global.exception.code.status.UserErrorStatus._PASSWORD_NOT_MATCHES;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserService userService;

    /**
     * 회원가입 처리
     * - 비밀번호 확인 여부를 검증
     * - 중복 이메일 가입을 차단
     * - 사용자를 저장하고 생성된 사용자 번호를 반환
     */
    public String execute(SignUpRequest request) {
        if(!request.isCheckedPassword()) {
            throw new RestApiException(_PASSWORD_NOT_MATCHES);
        }

        if (userService.isAlreadyRegistered(request.email())) {
            throw new RestApiException(ALREADY_REGISTERED_EMAIL);
        }

        User savedUser = userService.save(request);
        return savedUser.getUserNo();
    }
}
