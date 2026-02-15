package com.project.dorumdorum.domain.room.ui.spec;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Room")
public interface DecideApplicationRequestApiSpec {

    @Operation(
            summary = "방 참여 요청 승인 API",
            description = "방 참여 요청(requestNo)을 승인합니다. 승인 시 대상 사용자는 방에 참여 처리됩니다."
    )
    @PostMapping("/api/rooms/{roomNo}/request/{requestNo}/approve")
    ResponseEntity<Void> approve(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "방 번호") String roomNo,
            @Parameter(description = "참여 요청 번호") String requestNo
    );

    @Operation(
            summary = "방 참여 요청 거절 API",
            description = "방 참여 요청(requestNo)을 거절합니다. 거절 시 요청은 무효 처리됩니다."
    )
    @PostMapping("/api/request/{requestNo}/reject")
    ResponseEntity<Void> reject(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "참여 요청 번호") String requestNo
    );
}
