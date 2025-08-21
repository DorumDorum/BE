package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.InviteRoomRequest;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface InviteRoomRequestApiSpec {

    @Operation(
            summary = "방 초대 요청 API",
            description = "특정 유저에게 방 초대를 보냅니다. " +
                    "방 번호(roomNo)와 초대 대상 사용자 번호(toUser)를 Path로 전달하고, " +
                    "초대 메시지(introduction)는 Body로 전달합니다."
    )
    @PostMapping("/api/rooms/{roomNo}/invite-request/user/{toUser}")
    BaseResponse<Void> invite(
            @Parameter(hidden = true) Long userNo,
            @Parameter(description = "방 번호") Long roomNo,
            @Parameter(description = "초대 대상 사용자 번호") Long toUser,
            @RequestBody(
                    description = "초대 요청 바디",
                    required = true
            ) InviteRoomRequest request
    );
}
