package com.project.dorumdorum.domain.room.ui;

import com.project.dorumdorum.domain.room.application.usecase.KickRoommateUseCase;
import com.project.dorumdorum.domain.room.ui.spec.KickRoommateApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KickRoommateController implements KickRoommateApiSpec {

    private final KickRoommateUseCase kickRoommateUseCase;

    @Override
    public ResponseEntity<Void> kick(
            @CurrentUser String requesterNo,
            @PathVariable String roomNo,
            @PathVariable String kickedUserNo
    ) {
        kickRoommateUseCase.execute(requesterNo, roomNo, kickedUserNo);
        return ResponseEntity.ok().build();
    }
}
