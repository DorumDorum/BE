package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.DeleteRoomUseCase;
import com.project.dorumdorum.domain.room.ui.spec.DeleteRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteRoomController implements DeleteRoomApiSpec {

    private final DeleteRoomUseCase deleteRoomUseCase;

    @Override
    public ResponseEntity<Void> delete(
            @CurrentUser String requesterNo,
            @PathVariable String roomNo
    ) {
        deleteRoomUseCase.execute(requesterNo, roomNo);
        return ResponseEntity.noContent().build();
    }
}
