package com.project.dorumdorum.domain.chat.presence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisPresenceRepository implements PresenceRepository {

    private static final String PREFIX = "PRESENCE:";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void save(PresenceSnapshot snapshot, long ttlSeconds) {
        try {
            String value = objectMapper.writeValueAsString(snapshot);
            redisTemplate.opsForValue().set(buildKey(snapshot.userId()), value, Duration.ofSeconds(ttlSeconds));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Presence serialize failed", e);
        }
    }

    @Override
    public Optional<PresenceSnapshot> find(Long userId) {
        String value = redisTemplate.opsForValue().get(buildKey(userId));
        if (value == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(value, PresenceSnapshot.class));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Presence deserialize failed", e);
        }
    }

    @Override
    public void delete(Long userId) {
        redisTemplate.delete(buildKey(userId));
    }

    private String buildKey(Long userId) {
        return PREFIX + userId;
    }
}
