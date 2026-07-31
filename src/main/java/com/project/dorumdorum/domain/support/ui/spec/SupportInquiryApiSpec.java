package com.project.dorumdorum.domain.support.ui.spec;

import com.project.dorumdorum.domain.support.application.dto.request.CreateSupportInquiryRequest;
import com.project.dorumdorum.domain.support.application.dto.response.SupportInquiryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Support")
public interface SupportInquiryApiSpec {

    @Operation(summary = "문의 접수 API", description = "사용자의 고객 문의를 접수합니다.")
    @PostMapping("/api/support/inquiries")
    ResponseEntity<SupportInquiryResponse> create(
            @Parameter(hidden = true) String userNo,
            @Valid @RequestBody CreateSupportInquiryRequest request
    );

    @Operation(summary = "내 문의 목록 조회 API", description = "내가 접수한 고객 문의 목록을 조회합니다.")
    @GetMapping("/api/support/inquiries")
    ResponseEntity<List<SupportInquiryResponse>> loadAll(@Parameter(hidden = true) String userNo);

    @Operation(summary = "내 문의 상세 조회 API", description = "내가 접수한 고객 문의를 단건 조회합니다.")
    @GetMapping("/api/support/inquiries/{inquiryNo}")
    ResponseEntity<SupportInquiryResponse> load(
            @Parameter(hidden = true) String userNo,
            @PathVariable String inquiryNo
    );
}
