package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyLikedRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadMyLikedRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMyLikedRoomsController implements LoadMyLikedRoomsApiSpec {

    private final LoadMyLikedRoomsUseCase loadMyLikedRoomsUseCase;

    @Override
    public ResponseEntity<List<FindRoomsResponse>> loadLikedRooms(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadMyLikedRoomsUseCase.execute(userNo));
    }
}

