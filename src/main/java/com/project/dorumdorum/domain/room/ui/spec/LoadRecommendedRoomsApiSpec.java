package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.response.RecommendedRoomsPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Room")
public interface LoadRecommendedRoomsApiSpec {

    @Operation(
            summary = "나와 잘 맞는 방 목록 조회 API",
            description = "내 체크리스트와 비교해 65% 이상 일치하는 방을 매칭률 순으로 반환합니다. hasChecklist가 false면 체크리스트 미작성 상태입니다."
    )
    @GetMapping("/api/rooms/recommended")
    ResponseEntity<RecommendedRoomsPageResponse> loadRecommended(
            @Parameter(hidden = true) String userNo
    );
}
