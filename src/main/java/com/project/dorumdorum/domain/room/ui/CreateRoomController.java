package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.usecase.CreateRoomUseCase;
import com.project.dorumdorum.domain.room.ui.spec.CreateRoomApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CreateRoomController implements CreateRoomApiSpec {

    private final CreateRoomUseCase createRoomUseCase;

    @Override
    public ResponseEntity<Void> create(
            @CurrentUser String userNo,
            @RequestBody @Valid RoomCreateRequest request
    ) {
        String roomNo = createRoomUseCase.execute(userNo, request);
        return ResponseEntity.created(URI.create("/api/rooms/" + roomNo)).build();
    }
}
