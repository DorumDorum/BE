package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.response.RecommendedRoomsPageResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadRecommendedRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.LoadRecommendedRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadRecommendedRoomsController implements LoadRecommendedRoomsApiSpec {

    private final LoadRecommendedRoomsUseCase loadRecommendedRoomsUseCase;

    @Override
    public ResponseEntity<RecommendedRoomsPageResponse> loadRecommended(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadRecommendedRoomsUseCase.execute(userNo));
    }
}
