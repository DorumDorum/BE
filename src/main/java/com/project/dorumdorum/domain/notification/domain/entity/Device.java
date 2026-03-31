package com.project.dorumdorum.domain.notification.domain.entity;

import com.project.dorumdorum.global.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
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
@Table(
        name = "devices",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_device_user_device", columnNames = {"user_no", "device_id"})
        },
        indexes = {
                @Index(name = "idx_device_user_no", columnList = "user_no"),
                @Index(name = "idx_device_fcm_token", columnList = "fcm_token")
        }
)
public class Device extends BaseEntity {

    @Id
    @Tsid
    private String id;

    @Column(nullable = false)
    private String userNo;

    @Column(nullable = false)
    private String deviceId;

    private String fcmToken;

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void clearFcmToken() {
        this.fcmToken = "";
    }
}
