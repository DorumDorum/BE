package com.project.dorumdorum.domain.user.application.dto.response;

public record TokenReissueResponse (
        String accessToken,
        String refreshToken
) {}
