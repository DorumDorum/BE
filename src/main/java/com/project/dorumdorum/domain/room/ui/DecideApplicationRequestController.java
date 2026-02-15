package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.DecideApplicationRequestUseCase;
import com.project.dorumdorum.domain.room.ui.spec.DecideApplicationRequestApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DecideApplicationRequestController implements DecideApplicationRequestApiSpec {

    private final DecideApplicationRequestUseCase decideApplicationRequestUseCase;

    @Override
    public ResponseEntity<Void> approve(
            @CurrentUser String userNo,
            @PathVariable String roomNo,
            @PathVariable String requestNo
    ) {
        decideApplicationRequestUseCase.approve(userNo, roomNo, requestNo);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> reject(
            @CurrentUser String userNo,
            @PathVariable String requestNo
    ) {
        decideApplicationRequestUseCase.reject(userNo, requestNo);
        return ResponseEntity.ok().build();
    }
}
