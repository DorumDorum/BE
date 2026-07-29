package com.project.dorumdorum.domain.roommate.ui.spec;

import com.project.dorumdorum.domain.roommate.application.dto.response.RoommateHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Roommate")
public interface LoadMyRoommateHistoryApiSpec {

    @Operation(summary = "내 룸메이트 기록 조회 API", description = "현재 및 과거에 함께 방을 확정했던 룸메이트 기록을 조회합니다.")
    @GetMapping("/api/users/me/roommate-history")
    ResponseEntity<List<RoommateHistoryResponse>> loadHistory(@Parameter(hidden = true) String userNo);
}
