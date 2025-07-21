package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.LoginRequest;
import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.dto.response.LoginResponse;
import com.project.dorumdorum.domain.user.application.usecase.UserAuthUseCase;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {

    private final UserAuthUseCase userAuthUseCase;

    @PostMapping("/sign-up")
    public BaseResponse<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        userAuthUseCase.signUp(request);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return BaseResponse.onSuccess(userAuthUseCase.login(request));
    }

    @DeleteMapping("/logout")
    public BaseResponse<Void> logout(HttpServletRequest request) {
        userAuthUseCase.logout(request);
        return BaseResponse.onSuccess();
    }
}
