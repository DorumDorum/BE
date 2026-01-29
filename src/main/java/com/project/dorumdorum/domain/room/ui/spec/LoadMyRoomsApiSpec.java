package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.domain.entity.Tag;
import com.project.dorumdorum.global.common.BaseResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Room")
public interface LoadMyRoomsApiSpec {

    @Operation(
            summary = "내가 속한 방 조회 API"
    )
    @GetMapping("/api/rooms/me")
    BaseResponse<FindRoomsResponse> load(
            @Parameter(hidden = true) Long userNo
    );
}
