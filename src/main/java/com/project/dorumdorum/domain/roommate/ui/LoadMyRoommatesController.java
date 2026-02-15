package com.project.dorumdorum.domain.roommate.ui;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.application.usecase.LoadMyRoommatesUseCase;
import com.project.dorumdorum.domain.roommate.ui.spec.LoadMyRoommatesApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadMyRoommatesController implements LoadMyRoommatesApiSpec {

    private final LoadMyRoommatesUseCase loadMyRoommatesUseCase;

    @Override
    public ResponseEntity<List<MyRoommateResponse>> load(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadMyRoommatesUseCase.execute(userNo));
    }
}
