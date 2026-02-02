package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.user.application.usecase.UpdateUserChecklistUseCase;
import com.project.dorumdorum.domain.user.ui.spec.UpdateUserChecklistApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateUserChecklistController implements UpdateUserChecklistApiSpec {

    private final UpdateUserChecklistUseCase updateUserChecklistUseCase;

    @Override
    public BaseResponse<Void> update(
            @CurrentUser Long userNo,
            @org.springframework.web.bind.annotation.RequestBody @Valid UpdateUserChecklistRequest request
    ) {
        updateUserChecklistUseCase.execute(userNo, request);
        return BaseResponse.onSuccess();
    }
}
