package com.project.dorumdorum.domain.notification.infra.repository;

import com.project.dorumdorum.domain.notification.domain.entity.UserPresence;
import com.project.dorumdorum.domain.notification.domain.repository.UserPresenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUserPresenceRepository implements UserPresenceRepository {

    private static final String KEY_PREFIX = "notification:presence:";
    private static final Duration TTL = Duration.ofSeconds(60);

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setOnline(String userNo) {
        String key = KEY_PREFIX + userNo;
        String current = redisTemplate.opsForValue().get(key);
        UserPresence presence = UserPresence.fromRedisValue(current);

        UserPresence newPresence = presence.kind() == UserPresence.PresenceKind.IN_CHATROOM
                ? presence
                : UserPresence.online();

        redisTemplate.opsForValue().set(key, newPresence.toRedisValue(), TTL);
    }

    @Override
    public void setInChatroom(String userNo, String messageRoomNo) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + userNo,
                UserPresence.inChatroom(messageRoomNo).toRedisValue(),
                TTL
        );
    }

    @Override
    public void setOffline(String userNo) {
        redisTemplate.delete(KEY_PREFIX + userNo);
    }

    @Override
    public UserPresence getPresence(String userNo) {
        String value = redisTemplate.opsForValue().get(KEY_PREFIX + userNo);
        return UserPresence.fromRedisValue(value);
    }
}
