package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.EXPIRED_MEMBER_JWT;
import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.LOGIN_ERROR;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse execute(LoginRequest request) {
        User user = userService.findByEmail(request.email());

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new RestApiException(LOGIN_ERROR);

        String accessToken = tokenProvider.createAccessToken(user.getUserNo(), user.getRole());
        String refreshToken = tokenProvider.createRefreshToken(user.getUserNo(), user.getRole());

        Duration tokenExpiration = tokenProvider.getRemainingDuration(refreshToken)
                .orElseThrow(() -> new RestApiException(EXPIRED_MEMBER_JWT));
        refreshTokenService.saveRefreshToken(user.getUserNo(), refreshToken, tokenExpiration);

        return new LoginResponse(accessToken, refreshToken);
    }
}
