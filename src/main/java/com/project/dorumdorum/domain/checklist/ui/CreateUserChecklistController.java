package com.project.dorumdorum.domain.checklist.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.usecase.CreateUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.ui.spec.CreateUserChecklistApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateUserChecklistController implements CreateUserChecklistApiSpec {

    private final CreateUserChecklistUseCase createUserChecklistUseCase;

    @Override
    public BaseResponse<Void> create(
            @CurrentUser String userNo,
            @RequestBody @Valid CreateUserChecklistRequest request
    ) {
        createUserChecklistUseCase.execute(userNo, request);
        return BaseResponse.onSuccess();
    }
}
