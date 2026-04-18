package com.project.dorumdorum.domain.room.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Room")
public interface DeleteRoomApiSpec {

    @Operation(
            summary = "방 삭제 API",
            description = "방장이 방 모집을 삭제합니다. 룸메이트가 없는 경우(방장 단독)에만 삭제 가능합니다."
    )
    @DeleteMapping("/api/rooms/{roomNo}")
    ResponseEntity<Void> delete(
            @Parameter(hidden = true) String requesterNo,
            @PathVariable String roomNo
    );
}
