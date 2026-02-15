package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface CreateRoomApiSpec {

    @Operation(
            summary = "방 생성 API",
            description = "새로운 방을 생성합니다. " +
                    "로그인한 사용자의 userNo를 기준으로 방이 소유되며, " +
                    "방 이름/설명/최대 인원 정보를 함께 전달합니다."
    )
    @PostMapping("/api/rooms")
    ResponseEntity<Void> create(
            @Parameter(hidden = true) String userNo,
            @RequestBody(
                    description = "생성할 방 정보 (방 타입, 수용 인원 수, 방 이름, 태그 리스트)",
                    required = true
            )
            RoomCreateRequest request
    );

}
