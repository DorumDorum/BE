package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.usecase.ApplyRoomUseCase;
import com.project.dorumdorum.domain.room.ui.spec.ApplyRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApplyRoomController implements ApplyRoomApiSpec {

    private final ApplyRoomUseCase applyRoomUseCase;

    @Override
    public ResponseEntity<Void> join(
            @CurrentUser String userNo,
            @PathVariable String roomNo,
            @RequestBody @Valid JoinRoomRequest request
    ) {
        String requestNo = applyRoomUseCase.execute(userNo, roomNo, request);
        return ResponseEntity.created(URI.create("/api/rooms/" + roomNo + "/request/" + requestNo)).build();
    }
}
