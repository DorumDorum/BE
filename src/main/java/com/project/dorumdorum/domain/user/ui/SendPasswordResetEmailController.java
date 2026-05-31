package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.SendPasswordResetEmailUseCase;
import com.project.dorumdorum.domain.user.ui.spec.SendPasswordResetEmailApiSpec;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class SendPasswordResetEmailController implements SendPasswordResetEmailApiSpec {

    private final SendPasswordResetEmailUseCase sendPasswordResetEmailUseCase;

    @Override
    public ResponseEntity<Void> send(
            @NotBlank @RequestParam String email
    ) {
        sendPasswordResetEmailUseCase.send(email);
        return ResponseEntity.ok().build();
    }
}
