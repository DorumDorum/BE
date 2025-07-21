package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.EmailVerificationUseCase;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailVerificationUseCase emailVerificationUseCase;

    @PostMapping("/send")
    public BaseResponse<Void> sendEmailVerification(@RequestParam String email) {
        emailVerificationUseCase.send(email);
        return BaseResponse.onSuccess();
    }

    @PostMapping("/verify")
    public BaseResponse<Void> verifyEmail(@RequestParam String email, @RequestParam String code) {
        emailVerificationUseCase.verify(email, code);
        return BaseResponse.onSuccess();
    }
}
