package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface ApplyRoomApiSpec {

    @Operation(
            summary = "방 참여 요청 API",
            description = "현재 로그인한 사용자가 특정 방(roomNo)에 참여 요청을 보냅니다. "
                    + "방 번호(roomNo)는 Path로 전달하고, "
                    + "소개 메시지(introduction)는 Body로 전달합니다."
    )
    @PostMapping("/api/rooms/{roomNo}/request")
    ResponseEntity<Void> join(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "방 번호") String roomNo,
            @RequestBody(
                    description = "참여 요청 바디(소개 메시지 포함)",
                    required = true
            ) JoinRoomRequest request
    );
}
