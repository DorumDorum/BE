package com.project.dorumdorum.domain.user.infra.repository;

import com.project.dorumdorum.domain.user.domain.repository.PasswordResetCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisPasswordResetCodeRepository implements PasswordResetCodeRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PASSWORD_RESET = "PASSWORD_RESET:";

    @Override
    public void save(String email, String code) {
        redisTemplate.opsForValue().set(PASSWORD_RESET + email, code, Duration.ofMinutes(10));
    }

    @Override
    public Optional<String> findByEmail(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PASSWORD_RESET + email));
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(PASSWORD_RESET + email);
    }
}
