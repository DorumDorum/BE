package com.project.dorumdorum.domain.support.application.usecase;

import com.project.dorumdorum.domain.support.application.dto.request.CreateSupportInquiryRequest;
import com.project.dorumdorum.domain.support.application.dto.response.SupportInquiryResponse;
import com.project.dorumdorum.domain.support.domain.entity.SupportInquiry;
import com.project.dorumdorum.domain.support.domain.entity.SupportInquiryStatus;
import com.project.dorumdorum.domain.support.domain.repository.SupportInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSupportInquiryUseCase {

    private final SupportInquiryRepository supportInquiryRepository;

    @Transactional
    public SupportInquiryResponse execute(String userNo, CreateSupportInquiryRequest request) {
        SupportInquiry inquiry = SupportInquiry.builder()
                .userNo(userNo)
                .category(request.category())
                .message(request.message().trim())
                .status(SupportInquiryStatus.RECEIVED)
                .build();
        return SupportInquiryResponse.from(supportInquiryRepository.save(inquiry));
    }
}
