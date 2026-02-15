package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.CheckMyRoomResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Room")
public interface CheckMyRoomApiSpec {

    @Operation(
            summary = "참여한 방 존재 여부 검사 API"
    )
    @GetMapping("/api/rooms/me/exists")
    ResponseEntity<CheckMyRoomResponse> check(
            @Parameter(hidden = true) String userNo
    );
}
