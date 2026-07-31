package com.project.dorumdorum.domain.roommate.ui;

import com.project.dorumdorum.domain.roommate.application.dto.response.RoommateHistoryResponse;
import com.project.dorumdorum.domain.roommate.application.usecase.LoadMyRoommateHistoryUseCase;
import com.project.dorumdorum.domain.roommate.ui.spec.LoadMyRoommateHistoryApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMyRoommateHistoryController implements LoadMyRoommateHistoryApiSpec {

    private final LoadMyRoommateHistoryUseCase loadMyRoommateHistoryUseCase;

    @Override
    public ResponseEntity<List<RoommateHistoryResponse>> loadHistory(@CurrentUser String userNo) {
        return ResponseEntity.ok(loadMyRoommateHistoryUseCase.execute(userNo));
    }
}
