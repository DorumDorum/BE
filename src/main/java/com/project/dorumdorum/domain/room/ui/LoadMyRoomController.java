package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadMyRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyRoomController implements LoadMyRoomsApiSpec {

    private final LoadMyRoomsUseCase loadMyRoomsUseCase;

    @Override
    public ResponseEntity<FindRoomsResponse> load(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadMyRoomsUseCase.execute(userNo));
    }
}
