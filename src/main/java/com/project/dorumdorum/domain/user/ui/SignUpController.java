package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.usecase.SignUpUseCase;
import com.project.dorumdorum.domain.user.ui.spec.SignUpApiSpec;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SignUpController implements SignUpApiSpec {

    private final SignUpUseCase signUpUseCase;

    @Override
    public ResponseEntity<Void> signUp(
            @RequestBody @Valid SignUpRequest request
    ) {
        String userNo = signUpUseCase.execute(request);
        return ResponseEntity.created(URI.create("/api/users/" + userNo + "/profile")).build();
    }
}
