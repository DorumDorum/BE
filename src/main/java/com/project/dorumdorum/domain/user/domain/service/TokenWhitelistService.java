package com.project.dorumdorum.domain.user.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenWhitelistService {

    private final RedisTemplate<String, String> redisTemplate;

    private final static String whitelistPrefix = "WHITELIST:";

    public boolean isWhitelistToken(String token) {
        String savedToken = redisTemplate.opsForValue().get(whitelistPrefix + token);

        if(savedToken == null)
            return false;

        return Objects.equals(savedToken, token);
    }


    public void whitelist(String token, Duration timeout) {
        redisTemplate.opsForValue().set(whitelistPrefix + token, "", timeout);
    }

    public void deleteWhitelistToken(String token) {
        redisTemplate.delete(whitelistPrefix + token);
    }
}
