package com.project.dorumdorum.global.ratelimit;

import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.properties.RateLimitProperties;
import com.project.dorumdorum.global.properties.RateLimitRuleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._INTERNAL_SERVER_ERROR;

@Component
@RequiredArgsConstructor
public class RateLimitPolicyRegistry {

    private final RateLimitProperties rateLimitProperties;

    public RateLimitRule get(String tag) {
        RateLimitRuleProperties rule = rateLimitProperties.getRules().get(tag);
        if (rule == null) {
            throw new RestApiException(_INTERNAL_SERVER_ERROR);
        }

        return new RateLimitRule(
                rule.getPermitsPerWindow(),
                rule.getWindowMillis(),
                rule.getTtlSeconds()
        );
    }
}
