package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.dto.response.NotificationSettingResponse;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationSetting;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoadNotificationSettingUseCase {

    private final NotificationSettingRepository notificationSettingRepository;

    @Transactional(readOnly = true)
    public NotificationSettingResponse execute(String userNo) {
        NotificationSetting setting = notificationSettingRepository.findByUserNo(userNo)
                .orElseGet(() -> NotificationSetting.defaultFor(userNo));
        return NotificationSettingResponse.from(setting);
    }
}
