package com.project.dorumdorum.domain.notification.application.usecase;

import com.project.dorumdorum.domain.notification.application.dto.request.NotificationSettingRequest;
import com.project.dorumdorum.domain.notification.application.dto.response.NotificationSettingResponse;
import com.project.dorumdorum.domain.notification.domain.entity.NotificationSetting;
import com.project.dorumdorum.domain.notification.domain.repository.NotificationSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateNotificationSettingUseCase {

    private final NotificationSettingRepository notificationSettingRepository;

    @Transactional
    public NotificationSettingResponse execute(String userNo, NotificationSettingRequest request) {
        NotificationSetting setting = notificationSettingRepository.findByUserNo(userNo)
                .orElseGet(() -> NotificationSetting.defaultFor(userNo));
        setting.update(request);
        NotificationSetting saved = notificationSettingRepository.save(setting);
        return NotificationSettingResponse.from(saved);
    }
}
