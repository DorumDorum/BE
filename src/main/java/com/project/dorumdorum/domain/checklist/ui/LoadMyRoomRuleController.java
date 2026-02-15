package com.project.dorumdorum.domain.checklist.ui;

import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import com.project.dorumdorum.domain.checklist.application.usecase.LoadMyRoomRuleUseCase;
import com.project.dorumdorum.domain.checklist.ui.spec.LoadMyRoomRuleApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyRoomRuleController implements LoadMyRoomRuleApiSpec {

    private final LoadMyRoomRuleUseCase loadMyRoomRuleUseCase;

    @Override
    public ResponseEntity<MyRoomRuleResponse> load(
            @PathVariable String roomNo
    ) {
        return ResponseEntity.ok(loadMyRoomRuleUseCase.execute(roomNo));
    }
}
