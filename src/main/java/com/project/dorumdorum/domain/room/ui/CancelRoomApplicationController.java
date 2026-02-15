package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.CancelRoomApplicationUseCase;
import com.project.dorumdorum.domain.room.ui.spec.CancelRoomApplicationApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CancelRoomApplicationController implements CancelRoomApplicationApiSpec {

    private final CancelRoomApplicationUseCase cancelRoomApplicationUseCase;

    @Override
    public ResponseEntity<Void> cancel(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        cancelRoomApplicationUseCase.execute(userNo, roomNo);
        return ResponseEntity.ok().build();
    }
}

