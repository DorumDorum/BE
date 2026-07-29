package com.project.dorumdorum.domain.support.application.usecase;

import com.project.dorumdorum.domain.support.application.dto.response.SupportInquiryResponse;
import com.project.dorumdorum.domain.support.domain.repository.SupportInquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadMySupportInquiriesUseCase {

    private final SupportInquiryRepository supportInquiryRepository;

    @Transactional(readOnly = true)
    public List<SupportInquiryResponse> execute(String userNo) {
        return supportInquiryRepository.findByUserNoAndDeletedAtIsNullOrderByCreatedAtDescInquiryNoDesc(userNo)
                .stream()
                .map(SupportInquiryResponse::from)
                .toList();
    }
}
