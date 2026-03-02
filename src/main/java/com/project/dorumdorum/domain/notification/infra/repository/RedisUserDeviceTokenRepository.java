package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.vo.Device;
import com.project.dorumdorum.domain.notification.domain.repository.UserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisUserDeviceTokenRepository implements UserDeviceTokenRepository {

    private static final String DEVICE_KEY_PREFIX = "notification:device:";
    private static final String USER_DEVICES_KEY_PREFIX = "notification:user:devices:";
    private static final Duration DEFAULT_TTL = Duration.ofDays(60);

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String userNo, String deviceId, String fcmToken) {
        if (userNo == null || userNo.isBlank() || deviceId == null || deviceId.isBlank())
            return;

        String deviceKey = deviceKey(userNo, deviceId);
        String userDevicesKey = userDevicesKey(userNo);

        redisTemplate.opsForSet().add(userDevicesKey, deviceId);
        redisTemplate.opsForValue().set(deviceKey, fcmToken != null ? fcmToken : "", DEFAULT_TTL);
    }

    @Override
    public List<Device> getDevices(String userNo) {
        if (userNo == null || userNo.isBlank())
            return List.of();

        String userDevicesKey = userDevicesKey(userNo);
        Set<String> deviceIds = redisTemplate.opsForSet().members(userDevicesKey);
        if (deviceIds == null || deviceIds.isEmpty())
            return List.of();

        List<Device> result = new ArrayList<>();
        for (String deviceId : deviceIds) {
            String deviceKey = deviceKey(userNo, deviceId);
            String fcmToken = redisTemplate.opsForValue().get(deviceKey);
            if (fcmToken == null) {
                redisTemplate.opsForSet().remove(userDevicesKey, deviceId);
                continue;
            }
            result.add(new Device(deviceId, fcmToken));
        }
        return result;
    }

    @Override
    public void remove(String userNo, String deviceId) {
        if (userNo == null || userNo.isBlank() || deviceId == null || deviceId.isBlank())
            return;
        redisTemplate.delete(deviceKey(userNo, deviceId));
        redisTemplate.opsForSet().remove(userDevicesKey(userNo), deviceId);
    }

    private static String deviceKey(String userNo, String deviceId) {
        return DEVICE_KEY_PREFIX + userNo + ":" + deviceId;
    }

    private static String userDevicesKey(String userNo) {
        return USER_DEVICES_KEY_PREFIX + userNo;
    }
}
