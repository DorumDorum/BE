package com.project.dorumdorum.domain.room.ui.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Room")
public interface KickRoommateApiSpec {

    @Operation(
            summary = "룸메이트 강퇴 API",
            description = "방장이 특정 룸메이트를 강퇴합니다. 강퇴된 멤버는 방과 채팅방에서 자동으로 퇴장 처리됩니다."
    )
    @DeleteMapping("/api/rooms/{roomNo}/members/{kickedUserNo}")
    ResponseEntity<Void> kick(
            @Parameter(hidden = true) String requesterNo,
            @PathVariable String roomNo,
            @PathVariable String kickedUserNo
    );
}
