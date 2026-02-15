package com.project.dorumdorum.domain.room.ui.spec;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Room")
public interface CancelRoomApplicationApiSpec {

    @Operation(
            summary = "방 지원 요청 취소 API",
            description = "현재 로그인한 사용자가 특정 방에 보낸 가입(지원) 요청을 취소합니다."
    )
    @DeleteMapping("/api/rooms/{roomNo}/request")
    ResponseEntity<Void> cancel(
            @Parameter(hidden = true) String userNo,
            @Parameter(description = "가입 요청을 취소할 방 번호") @PathVariable String roomNo
    );
}

