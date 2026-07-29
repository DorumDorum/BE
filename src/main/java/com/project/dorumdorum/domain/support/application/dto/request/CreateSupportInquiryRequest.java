package com.project.dorumdorum.domain.support.application.dto.request;

import com.project.dorumdorum.domain.support.domain.entity.SupportInquiryCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSupportInquiryRequest(
        @NotNull SupportInquiryCategory category,
        @NotBlank @Size(max = 500) String message
) {
}
