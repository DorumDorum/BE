package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.RoomDetailResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadRoomDetailUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadRoomDetailApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadRoomDetailController implements LoadRoomDetailApiSpec {

    private final LoadRoomDetailUseCase loadRoomDetailUseCase;

    @Override
    public ResponseEntity<RoomDetailResponse> load(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        return ResponseEntity.ok(loadRoomDetailUseCase.execute(userNo, roomNo));
    }
}
