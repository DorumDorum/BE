package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.ConfirmRoomAssignmentUseCase;
import com.project.dorumdorum.domain.room.ui.spec.ConfirmRoomAssignmentApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfirmRoomAssignmentController implements ConfirmRoomAssignmentApiSpec {

    private final ConfirmRoomAssignmentUseCase confirmRoomAssignmentUseCase;

    @Override
    public ResponseEntity<Void> confirm(
            @CurrentUser String userNo,
            @RequestParam String roomNo
    ) {
        confirmRoomAssignmentUseCase.execute(userNo, roomNo);
        return ResponseEntity.ok().build();
    }
}
