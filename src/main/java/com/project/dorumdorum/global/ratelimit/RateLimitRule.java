package com.project.dorumdorum.global.ratelimit;

public record RateLimitRule(
        long permitsPerWindow,
        long windowMillis,
        long ttlSeconds
) {
}
