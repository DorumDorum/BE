package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.usecase.SendVerificationEmailUseCase;
import com.project.dorumdorum.domain.user.ui.spec.SendVerificationEmailApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendVerificationEmailController implements SendVerificationEmailApiSpec {

    private final SendVerificationEmailUseCase sendVerificationEmailUseCase;

    @Override
    public ResponseEntity<Void> send(
            @RequestParam String email
    ) {
        sendVerificationEmailUseCase.send(email);
        return ResponseEntity.ok().build();
    }
}
