package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Room")
public interface LoadMyAppliedRoomsApiSpec {

    @Operation(
            summary = "내가 지원한 방 목록 조회 API",
            description = "현재 로그인한 사용자가 지원한 방 목록을 조회합니다."
    )
    @GetMapping("/api/rooms/me/applied")
    ResponseEntity<List<FindRoomsResponse>> loadAppliedRooms(
            @Parameter(hidden = true) String userNo
    );
}

