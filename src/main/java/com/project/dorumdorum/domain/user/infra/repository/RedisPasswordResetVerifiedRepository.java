package com.project.dorumdorum.domain.user.infra.repository;

import com.project.dorumdorum.domain.user.domain.repository.PasswordResetVerifiedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisPasswordResetVerifiedRepository implements PasswordResetVerifiedRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PASSWORD_RESET_VERIFIED = "PASSWORD_RESET_VERIFIED:";
    private static final String VERIFIED_VALUE = "verified";

    @Override
    public void save(String email) {
        redisTemplate.opsForValue().set(PASSWORD_RESET_VERIFIED + email, VERIFIED_VALUE, Duration.ofMinutes(10));
    }

    @Override
    public boolean existsByEmail(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PASSWORD_RESET_VERIFIED + email));
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(PASSWORD_RESET_VERIFIED + email);
    }
}
