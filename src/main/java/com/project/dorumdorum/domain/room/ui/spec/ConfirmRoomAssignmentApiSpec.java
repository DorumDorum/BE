package com.project.dorumdorum.domain.room.ui.spec;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Room")
public interface ConfirmRoomAssignmentApiSpec {

    @Operation(
            summary = "방 배정 확정 API",
            description = "룸메이트가 자신의 방 배정을 확정합니다. 모든 룸메이트가 확정하면 방 상태가 COMPLETED로 변경됩니다."
    )
    @PostMapping("/api/rooms/me/confirm")
    ResponseEntity<Void> confirm(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "배정 확정하려는 방 번호") @RequestParam String roomNo
    );
}
