package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.ALREADY_REGISTERED_EMAIL;
import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._PASSWORD_NOT_MATCHES;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserService userService;

    public void execute(SignUpRequest request) {
        if(!request.isCheckedPassword()) {
            throw new RestApiException(_PASSWORD_NOT_MATCHES);
        }

        if (userService.isAlreadyRegistered(request.email())) {
            throw new RestApiException(ALREADY_REGISTERED_EMAIL);
        }

        userService.save(request);
    }
}
