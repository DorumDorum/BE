package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface RoomLikeApiSpec {

    @Operation(summary = "방 관심 등록(좋아요) API")
    @PostMapping("/api/rooms/{roomNo}/like")
    BaseResponse<Void> like(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "관심 등록할 방 번호") String roomNo
    );

    @Operation(summary = "방 관심 해제(좋아요 취소) API")
    @DeleteMapping("/api/rooms/{roomNo}/like")
    BaseResponse<Void> unlike(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "관심 해제할 방 번호") String roomNo
    );
}

