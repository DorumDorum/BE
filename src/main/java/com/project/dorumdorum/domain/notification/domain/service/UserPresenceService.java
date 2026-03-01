package com.project.dorumdorum.domain.notification.domain.service;

import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserPresenceService {

    private static final String KEY_PREFIX = "notification:presence:";
    private static final Duration TTL = Duration.ofSeconds(60);

    private final RedisTemplate<String, String> redisTemplate;

    public void setOnline(String userNo) {
        redisTemplate.opsForValue().set(KEY_PREFIX + userNo, UserPresence.online().toRedisValue(), TTL);
    }

    public void setInChatroom(String userNo, String messageRoomNo) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + userNo,
                UserPresence.inChatroom(messageRoomNo).toRedisValue(),
                TTL
        );
    }

    public void setOffline(String userNo) {
        redisTemplate.delete(KEY_PREFIX + userNo);
    }

    public UserPresence getPresence(String userNo) {
        String value = redisTemplate.opsForValue().get(KEY_PREFIX + userNo);
        return UserPresence.fromRedisValue(value);
    }
}
