package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Room")
public interface LoadMyRoommatesApiSpec {

    @Operation(
            summary = "내가 속한 방 룸메 조회 API"
    )
    @GetMapping("/api/rooms/me/roommates")
    BaseResponse<List<MyRoommateResponse>> load(
            @Parameter(hidden = true) Long userNo
    );
}
