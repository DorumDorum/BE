package com.project.dorumdorum.domain.notification.application.dto.response;

import com.project.dorumdorum.domain.notification.domain.entity.NotificationSetting;

public record NotificationSettingResponse(
        boolean enabled,
        boolean applicants,
        boolean applicantResult,
        boolean chat,
        boolean notice,
        boolean schedule
) {
    public static NotificationSettingResponse from(NotificationSetting setting) {
        return new NotificationSettingResponse(
                setting.isEnabled(),
                setting.isApplicants(),
                setting.isApplicantResult(),
                setting.isChat(),
                setting.isNotice(),
                setting.isSchedule()
        );
    }
}
