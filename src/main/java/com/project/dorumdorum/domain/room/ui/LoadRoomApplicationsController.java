package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadRoomApplicationsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadRoomApplicationsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadRoomApplicationsController implements LoadRoomApplicationsApiSpec {

    private final LoadRoomApplicationsUseCase loadRoomApplicationsUseCase;

    @Override
    public ResponseEntity<List<RoomRequestApplicationResponse>> loadApplications(
            @CurrentUser String userNo,
            @PathVariable String roomNo
    ) {
        return ResponseEntity.ok(loadRoomApplicationsUseCase.execute(userNo, roomNo));
    }
}
