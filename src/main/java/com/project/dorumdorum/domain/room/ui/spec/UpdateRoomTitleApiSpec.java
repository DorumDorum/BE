package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Room")
public interface UpdateRoomTitleApiSpec {

    @Operation(
            summary = "방 제목 수정 API"
    )
    @PutMapping("/api/rooms/me/title")
    ResponseEntity<Void> update(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "제목을 수정하려는 방 번호") @RequestParam String roomNo,
            @Parameter(description = "수정할 제목") @Valid @RequestBody UpdateRoomTitleRequest request
    );
}
