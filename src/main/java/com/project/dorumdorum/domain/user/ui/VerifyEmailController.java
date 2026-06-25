package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.VerifyEmailUseCase;
import com.project.dorumdorum.domain.user.ui.spec.VerifyEmailApiSpec;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class VerifyEmailController implements VerifyEmailApiSpec {

    private final VerifyEmailUseCase verifyEmailUseCase;

    @Override
    public ResponseEntity<Void> verifyEmail(
            @NotBlank @RequestParam String email,
            @NotBlank @RequestParam String code
    ) {
        verifyEmailUseCase.execute(email, code);
        return ResponseEntity.ok().build();
    }
}
