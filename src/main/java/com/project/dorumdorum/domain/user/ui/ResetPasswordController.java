package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.ResetPasswordRequest;
import com.project.dorumdorum.domain.user.application.usecase.ResetPasswordUseCase;
import com.project.dorumdorum.domain.user.ui.spec.ResetPasswordApiSpec;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController implements ResetPasswordApiSpec {

    private final ResetPasswordUseCase resetPasswordUseCase;

    @Override
    public ResponseEntity<Void> reset(
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        resetPasswordUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}
