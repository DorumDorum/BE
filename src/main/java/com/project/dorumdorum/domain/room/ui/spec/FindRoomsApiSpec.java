package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.ChecklistFilterRequest;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.global.pagination.CursorPage;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Room")
public interface FindRoomsApiSpec {

    @Operation(
            summary = "방 목록 조회 API",
            description = "체크리스트 및 방 조건을 기준으로 방 목록을 조회합니다. "
                    + "커서 기반 페이지네이션을 지원합니다."
    )
    @PostMapping("/api/rooms/search")
    ResponseEntity<CursorPage<FindRoomsResponse>> loadAll(
            @Parameter(description = "체크리스트 기반 방 검색 조건", required = true)
            @Valid @RequestBody ChecklistFilterRequest request
    );
}
