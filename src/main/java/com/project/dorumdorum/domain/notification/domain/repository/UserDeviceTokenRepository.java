package com.project.dorumdorum.domain.notification.domain.repository;

import com.project.dorumdorum.domain.notification.domain.vo.Device;

import java.util.List;

public interface UserDeviceTokenRepository {

    void save(String userNo, String deviceId, String fcmToken);

    List<Device> getDevices(String userNo);

    void remove(String userNo, String deviceId);
}
