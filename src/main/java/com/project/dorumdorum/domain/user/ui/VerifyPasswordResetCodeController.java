package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.VerifyPasswordResetCodeUseCase;
import com.project.dorumdorum.domain.user.ui.spec.VerifyPasswordResetCodeApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerifyPasswordResetCodeController implements VerifyPasswordResetCodeApiSpec {

    private final VerifyPasswordResetCodeUseCase verifyPasswordResetCodeUseCase;

    @Override
    public ResponseEntity<Void> verify(
            @RequestParam String email,
            @RequestParam String code
    ) {
        verifyPasswordResetCodeUseCase.execute(email, code);
        return ResponseEntity.ok().build();
    }
}
