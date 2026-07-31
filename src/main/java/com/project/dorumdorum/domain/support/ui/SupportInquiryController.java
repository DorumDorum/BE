package com.project.dorumdorum.domain.support.ui;

import com.project.dorumdorum.domain.support.application.dto.request.CreateSupportInquiryRequest;
import com.project.dorumdorum.domain.support.application.dto.response.SupportInquiryResponse;
import com.project.dorumdorum.domain.support.application.usecase.CreateSupportInquiryUseCase;
import com.project.dorumdorum.domain.support.application.usecase.LoadMySupportInquiriesUseCase;
import com.project.dorumdorum.domain.support.application.usecase.LoadSupportInquiryDetailUseCase;
import com.project.dorumdorum.domain.support.ui.spec.SupportInquiryApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SupportInquiryController implements SupportInquiryApiSpec {

    private final CreateSupportInquiryUseCase createSupportInquiryUseCase;
    private final LoadMySupportInquiriesUseCase loadMySupportInquiriesUseCase;
    private final LoadSupportInquiryDetailUseCase loadSupportInquiryDetailUseCase;

    @Override
    public ResponseEntity<SupportInquiryResponse> create(
            @CurrentUser String userNo,
            @Valid @RequestBody CreateSupportInquiryRequest request
    ) {
        return ResponseEntity.ok(createSupportInquiryUseCase.execute(userNo, request));
    }

    @Override
    public ResponseEntity<List<SupportInquiryResponse>> loadAll(@CurrentUser String userNo) {
        return ResponseEntity.ok(loadMySupportInquiriesUseCase.execute(userNo));
    }

    @Override
    public ResponseEntity<SupportInquiryResponse> load(
            @CurrentUser String userNo,
            @PathVariable String inquiryNo
    ) {
        return ResponseEntity.ok(loadSupportInquiryDetailUseCase.execute(userNo, inquiryNo));
    }
}
