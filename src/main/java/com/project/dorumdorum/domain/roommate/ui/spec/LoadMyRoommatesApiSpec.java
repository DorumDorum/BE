package com.project.dorumdorum.domain.roommate.ui.spec;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Roommate")
public interface LoadMyRoommatesApiSpec {

    @Operation(
            summary = "내가 속한 방 룸메 조회 API"
    )
    @GetMapping("/api/rooms/me/roommates")
    ResponseEntity<List<MyRoommateResponse>> load(
            @Parameter(hidden = true) String userNo
    );
}
