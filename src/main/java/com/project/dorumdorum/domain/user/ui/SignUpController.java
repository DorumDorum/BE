package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.usecase.SignUpUseCase;
import com.project.dorumdorum.domain.user.ui.spec.SignUpApiSpec;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpController implements SignUpApiSpec {

    private final SignUpUseCase signUpUseCase;

    @Override
    public BaseResponse<Void> signUp(
            @RequestBody @Valid SignUpRequest request
    ) {
        signUpUseCase.execute(request);
        return BaseResponse.onSuccess();
    }
}
