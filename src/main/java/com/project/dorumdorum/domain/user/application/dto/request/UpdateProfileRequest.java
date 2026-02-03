package com.project.dorumdorum.domain.user.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String nickname,
        @NotBlank String grade,
        @NotBlank String major
) { }
