package com.project.dorumdorum.domain.room.ui.spec;

import com.project.dorumdorum.domain.room.application.dto.request.RoomRelation;
import com.project.dorumdorum.domain.room.application.dto.request.RoomSort;
import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.global.pagination.CursorPage;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "Room")
public interface FindRoomsApiSpec {

    @Operation(
            summary = "방 목록 조회 API",
            description = "사용자의 관계, 방 타입, 인원 수, 정렬 조건 등을 기준으로 방 목록을 조회합니다. "
                    + "커서 기반 페이지네이션을 지원합니다."
    )
    @GetMapping("/api/rooms")
    ResponseEntity<CursorPage<FindRoomsResponse>> loadAll(
            @Parameter(description = "조회 기준 관계", required = true) RoomRelation relation,
            @Parameter(description = "방 타입 (복수 선택 가능)") List<RoomType> types,
            @Parameter(description = "최대 인원 수 필터 (복수 선택 가능)") List<Integer> capacities,
            @Parameter(description = "거주기간 필터 (복수 선택 가능)") List<ResidencePeriod> residencePeriods,
            @Parameter(description = "정렬 기준") RoomSort sort,
            @Parameter(description = "커서 값") String cursor
    );
}
