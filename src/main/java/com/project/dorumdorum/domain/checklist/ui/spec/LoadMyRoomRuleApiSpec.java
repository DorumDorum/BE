package com.project.dorumdorum.domain.checklist.ui.spec;

import com.project.dorumdorum.domain.checklist.application.dto.response.MyRoomRuleResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Checklist")
public interface LoadMyRoomRuleApiSpec {

    @Operation(
            summary = "내가 속한 방 규칙 조회 API"
    )
    @GetMapping("/api/rooms/{roomNo}/rule")
    ResponseEntity<MyRoomRuleResponse> load(
            @Parameter(description = "규칙 조회하려는 방 번호") String roomNo
    );
}
