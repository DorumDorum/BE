package com.project.dorumdorum.domain.notice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public record WriteNoticeRequest(
        @NotNull Long roomNo,
        @NotBlank String title,
        @NotBlank String content,
        @Nullable String imageFileName,
        @Nullable Long imageFileSize
) {

}
