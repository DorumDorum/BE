package com.project.dorumdorum.domain.notification.domain.entity;

import com.project.dorumdorum.domain.notification.application.dto.request.NotificationSettingRequest;
import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "notification_settings")
public class NotificationSetting extends BaseEntity {

    @Id
    @Tsid
    private String notificationSettingNo;

    @Column(nullable = false, unique = true)
    private String userNo;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean applicants;

    @Column(nullable = false)
    private boolean applicantResult;

    @Column(nullable = false)
    private boolean chat;

    @Column(nullable = false)
    private boolean notice;

    @Column(nullable = false)
    private boolean schedule;

    public static NotificationSetting defaultFor(String userNo) {
        return NotificationSetting.builder()
                .userNo(userNo)
                .enabled(true)
                .applicants(true)
                .applicantResult(true)
                .chat(true)
                .notice(true)
                .schedule(false)
                .build();
    }

    public void update(NotificationSettingRequest request) {
        this.enabled = request.enabled();
        this.applicants = request.applicants();
        this.applicantResult = request.applicantResult();
        this.chat = request.chat();
        this.notice = request.notice();
        this.schedule = request.schedule();
    }
}
