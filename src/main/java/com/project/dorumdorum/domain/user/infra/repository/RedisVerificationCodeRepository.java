package com.project.dorumdorum.domain.user.infra.repository;

import com.project.dorumdorum.domain.user.domain.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisVerificationCodeRepository implements VerificationCodeRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String EMAIL_VERIFICATION = "EMAIL_VERIFICATION:";

    @Override
    public void save(String email, String code) {
        redisTemplate.opsForValue().set(EMAIL_VERIFICATION + email, code, Duration.ofMinutes(10));
    }

    @Override
    public Optional<String> findByEmail(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(EMAIL_VERIFICATION + email));
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(EMAIL_VERIFICATION + email);
    }
}
