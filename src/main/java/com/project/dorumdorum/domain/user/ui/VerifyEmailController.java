package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.VerifyEmailUseCase;
import com.project.dorumdorum.domain.user.ui.spec.VerifyEmailApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerifyEmailController implements VerifyEmailApiSpec {

    private final VerifyEmailUseCase verifyEmailUseCase;

    @Override
    public ResponseEntity<Void> verifyEmail(
            @RequestParam String email,
            @RequestParam String code
    ) {
        verifyEmailUseCase.execute(email, code);
        return ResponseEntity.ok().build();
    }
}
