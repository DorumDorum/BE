package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Room")
public interface LoadMyRoomsApiSpec {

    @Operation(
            summary = "내가 속한 방 조회 API"
    )
    @GetMapping("/api/rooms/me")
    ResponseEntity<FindRoomsResponse> load(
            @Parameter(hidden = true) String userNo
    );
}
