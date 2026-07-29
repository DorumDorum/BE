package com.project.dorumdorum.domain.notification.application.dto.request;

public record NotificationSettingRequest(
        boolean enabled,
        boolean applicants,
        boolean applicantResult,
        boolean chat,
        boolean notice,
        boolean schedule
) {
}
