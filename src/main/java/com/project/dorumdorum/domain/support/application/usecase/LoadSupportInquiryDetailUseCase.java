package com.project.dorumdorum.domain.support.application.usecase;

import com.project.dorumdorum.domain.support.application.dto.response.SupportInquiryResponse;
import com.project.dorumdorum.domain.support.domain.repository.SupportInquiryRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoadSupportInquiryDetailUseCase {

    private final SupportInquiryRepository supportInquiryRepository;

    @Transactional(readOnly = true)
    public SupportInquiryResponse execute(String userNo, String inquiryNo) {
        return supportInquiryRepository.findByInquiryNoAndUserNoAndDeletedAtIsNull(inquiryNo, userNo)
                .map(SupportInquiryResponse::from)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }
}
