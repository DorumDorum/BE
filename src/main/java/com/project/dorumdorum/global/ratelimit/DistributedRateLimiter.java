package com.project.dorumdorum.global.ratelimit;

public interface DistributedRateLimiter {

    boolean tryAcquire(String key, long capacity, long refillTokensPerSecond, long requestedPermits);
}
