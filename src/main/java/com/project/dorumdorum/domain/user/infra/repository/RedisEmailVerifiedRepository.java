package com.project.dorumdorum.domain.user.infra.repository;

import com.project.dorumdorum.domain.user.domain.repository.EmailVerifiedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisEmailVerifiedRepository implements EmailVerifiedRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String EMAIL_VERIFIED = "EMAIL_VERIFIED:";
    private static final String VERIFIED_VALUE = "verified";

    @Override
    public void save(String email) {
        redisTemplate.opsForValue().set(EMAIL_VERIFIED + email, VERIFIED_VALUE, Duration.ofMinutes(30));
    }

    @Override
    public boolean existsByEmail(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(EMAIL_VERIFIED + email));
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(EMAIL_VERIFIED + email);
    }
}
