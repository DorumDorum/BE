package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Room")
public interface LoadRoomApplicationsApiSpec {

    @Operation(
            summary = "방 지원자 목록 조회 API",
            description = "특정 방에 대한 지원자(RoomRequest) 목록을 조회합니다. 방장만 조회 가능합니다."
    )
    @GetMapping("/api/rooms/{roomNo}/applications")
    BaseResponse<List<RoomRequestApplicationResponse>> loadApplications(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "방 번호") @PathVariable String roomNo
    );
}
