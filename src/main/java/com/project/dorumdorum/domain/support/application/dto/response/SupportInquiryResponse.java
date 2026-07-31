package com.project.dorumdorum.domain.support.application.dto.response;

import com.project.dorumdorum.domain.support.domain.entity.SupportInquiry;
import com.project.dorumdorum.domain.support.domain.entity.SupportInquiryCategory;
import com.project.dorumdorum.domain.support.domain.entity.SupportInquiryStatus;

import java.time.LocalDateTime;

public record SupportInquiryResponse(
        String inquiryNo,
        SupportInquiryCategory category,
        String message,
        SupportInquiryStatus status,
        LocalDateTime createdAt
) {
    public static SupportInquiryResponse from(SupportInquiry inquiry) {
        return new SupportInquiryResponse(
                inquiry.getInquiryNo(),
                inquiry.getCategory(),
                inquiry.getMessage(),
                inquiry.getStatus(),
                inquiry.getCreatedAt()
        );
    }
}
