package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyAppliedRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadMyAppliedRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMyAppliedRoomsController implements LoadMyAppliedRoomsApiSpec {

    private final LoadMyAppliedRoomsUseCase loadMyAppliedRoomsUseCase;

    @Override
    public ResponseEntity<List<FindRoomsResponse>> loadAppliedRooms(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadMyAppliedRoomsUseCase.execute(userNo));
    }
}

