package com.project.dorumdorum.domain.checklist.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.UpdateRoomRuleRequest;
import com.project.dorumdorum.domain.checklist.application.usecase.UpdateRoomRuleUseCase;
import com.project.dorumdorum.domain.checklist.ui.spec.UpdateRoomRuleApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateRoomRuleController implements UpdateRoomRuleApiSpec {

    private final UpdateRoomRuleUseCase updateRoomRuleUseCase;

    @Override
    public ResponseEntity<Void> update(
            @CurrentUser String userNo,
            @RequestParam String roomNo,
            @Valid @RequestBody UpdateRoomRuleRequest request
    ) {
        updateRoomRuleUseCase.execute(userNo, roomNo, request);
        return ResponseEntity.ok().build();
    }
}
