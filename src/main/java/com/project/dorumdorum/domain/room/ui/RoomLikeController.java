package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.RoomLikeUseCase;
import com.project.dorumdorum.domain.room.ui.spec.RoomLikeApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomLikeController implements RoomLikeApiSpec {

    private final RoomLikeUseCase roomLikeUseCase;

    @Override
    public ResponseEntity<Void> like(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        roomLikeUseCase.like(userNo, roomNo);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unlike(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        roomLikeUseCase.unlike(userNo, roomNo);
        return ResponseEntity.ok().build();
    }
}

