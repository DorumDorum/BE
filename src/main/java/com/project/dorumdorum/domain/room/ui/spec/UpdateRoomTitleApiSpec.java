package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import com.project.dorumdorum.global.common.BaseResponse;
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
    BaseResponse<Void> update(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "제목을 수정하려는 방 번호") @RequestParam String roomNo,
            @Parameter(description = "수정할 제목") @RequestBody UpdateRoomTitleRequest request
    );
}
