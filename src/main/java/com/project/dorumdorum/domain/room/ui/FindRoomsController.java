package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsPageResponse;
import com.project.dorumdorum.domain.room.application.usecase.FindRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.spec.FindRoomsApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FindRoomsController implements FindRoomsApiSpec {

    private final FindRoomsUseCase findRoomsUseCase;

    @Override
    public ResponseEntity<FindRoomsPageResponse> loadAll(
            @CurrentUser String userNo,
            @Valid @RequestBody ChecklistFilterRequest request
    ) {
        return ResponseEntity.ok(findRoomsUseCase.execute(userNo, request));
    }
}
