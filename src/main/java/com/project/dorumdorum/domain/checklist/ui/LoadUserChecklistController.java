package com.project.dorumdorum.domain.checklist.ui;

import com.project.dorumdorum.domain.checklist.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.checklist.application.usecase.LoadUserChecklistUseCase;
import com.project.dorumdorum.domain.checklist.ui.spec.LoadUserChecklistApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadUserChecklistController implements LoadUserChecklistApiSpec {

    private final LoadUserChecklistUseCase loadUserChecklistUseCase;

    @Override
    public ResponseEntity<UserChecklistResponse> loadMyChecklist(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadUserChecklistUseCase.execute(userNo));
    }

    @Override
    public ResponseEntity<UserChecklistResponse> loadUserChecklist(
            @PathVariable String userNo
    ) {
        return ResponseEntity.ok(loadUserChecklistUseCase.execute(userNo));
    }
}
