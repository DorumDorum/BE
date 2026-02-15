package com.project.dorumdorum.domain.checklist.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateUserChecklistRequest;
import com.project.dorumdorum.domain.checklist.application.usecase.UpdateUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.ui.spec.UpdateUserChecklistApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateUserChecklistController implements UpdateUserChecklistApiSpec {

    private final UpdateUserChecklistUseCase updateUserChecklistUseCase;

    @Override
    public ResponseEntity<Void> update(
            @CurrentUser String userNo,
            @org.springframework.web.bind.annotation.RequestBody @Valid UpdateUserChecklistRequest request
    ) {
        updateUserChecklistUseCase.execute(userNo, request);
        return ResponseEntity.ok().build();
    }
}
