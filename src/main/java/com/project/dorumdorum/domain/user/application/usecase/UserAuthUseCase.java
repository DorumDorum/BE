package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.RefreshTokenService;
import com.project.dorumdorum.domain.user.domain.service.TokenBlacklistService;
import com.project.dorumdorum.domain.user.domain.service.TokenWhitelistService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthUseCase {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final TokenWhitelistService tokenWhitelistService;

    public void signUp(SignUpRequest request) {
        if (userService.isAlreadyRegistered(request.email()))
            throw new RestApiException(ALREADY_REGISTERED_EMAIL);

        userService.save(request);
    }

    public LoginResponse login(LoginRequest request) {
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

    public void logout(HttpServletRequest request) {
        String accessToken = tokenProvider.getToken(request)
                .orElseThrow(() -> new RestApiException(EMPTY_JWT));

        Long userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));

        Duration expiration = tokenProvider.getRemainingDuration(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ACCESS_TOKEN));

        tokenWhitelistService.deleteWhitelistToken(accessToken);
        refreshTokenService.deleteRefreshToken(userNo);
        tokenBlacklistService.blacklist(accessToken, expiration);
    }
}
