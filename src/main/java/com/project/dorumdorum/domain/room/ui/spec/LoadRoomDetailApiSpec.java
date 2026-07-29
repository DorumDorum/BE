package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.RoomDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Room")
public interface LoadRoomDetailApiSpec {

    @Operation(summary = "방 상세 조회 API", description = "방 상세 화면에 필요한 모집방 정보를 단건 조회합니다.")
    @GetMapping("/api/rooms/{roomNo}")
    ResponseEntity<RoomDetailResponse> load(
            @Parameter(hidden = true) String userNo,
            @PathVariable String roomNo
    );
}
